package com.app.bank.service;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.model.User;
import com.app.bank.repo.AccountRepository;
import com.app.bank.dto.request.*;
import com.app.bank.dto.response.AccountResponse;
import com.mongodb.DuplicateKeyException;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.math.BigDecimal;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionService transactionService;
    private final UserService userService;
    private final MongoTemplate mongoTemplate;

    public AccountService(AccountRepository accountRepository, TransactionService transactionService, UserService userService, MongoTemplate mongoTemplate) {
        this.accountRepository = accountRepository;
        this.transactionService = transactionService;
        this.userService = userService;
        this.mongoTemplate = mongoTemplate;
    }

    // ---------------------------------------- Verify Methods -----------------------------------------
    public void verifyOwnership(String accountNumber, String username) {
        Account account = getAccount(accountNumber);
        if (!account.getUsername().equals(username)) {
            throw new AccessDeniedException("You do not own this account.");
        }
    }
    
    private boolean validate(String username, String type) {
        return username != null && !username.isBlank()
            && type != null && !type.isBlank();
    }

    private Account findAccount(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account not found."));
    }

    private String generateAccountNumber() {
        return String.valueOf(ThreadLocalRandom.current().nextLong(1_000_000_000L, 10_000_000_000L));
    }
    
    // ---------------------------------------- Main Methods -----------------------------------------
    public void newAccount(String username, String type) {
        if (!validate(username, type)) {
            throw new BadRequestException("Account username and type are required.");
        }
       
        if (!userService.checkforUserName(username)) {
            throw new ResourceNotFoundException("User not found.");
        }

        if (accountRepository.findByUsername(username).size() >= 3) {
            throw new BadRequestException("Account limit reached. A user can only have up to 3 accounts.");
        }

        try {
            Account account = new Account(username, generateAccountNumber(), type);
            accountRepository.insert(account);
            mongoTemplate.update(User.class)
                .matching(Criteria.where("username").is(account.getUsername()))
                .apply(new Update().push("accounts").value(account))
                .first();
        } catch (DuplicateKeyException e) {
            throw new BadRequestException("Account creation failed, please try again.");
        }
    }

    private Account getAccount(String accountNumber) {
        return findAccount(accountNumber);
    }

    public AccountResponse getAccountResponse(String accountNumber) { 
        Account account = getAccount(accountNumber);
        return new AccountResponse(account);
    }

    @Transactional
    public void deleteAccount(String username, String accountNumber) {
        verifyOwnership(accountNumber, username);
        Account account = getAccount(accountNumber);
        accountRepository.delete(account);
        transactionService.deleteAccountTransactions(accountNumber);
    }

    //---------------------------------------- Transaction Methods -----------------------------------------
    
    public void depositAmount(String username, String accountNumber, BigDecimal amount) {
        verifyOwnership(accountNumber, username);
        
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Deposit amount must be greater than zero.");
        }
        Account account = getAccount(accountNumber);

        account.setBalance(account.getBalance().add(amount));
        transactionService.deposit(accountNumber, amount);
        accountRepository.save(account);
    }

    public void withdrawAmount(String username, String accountNumber, BigDecimal amount) {
        verifyOwnership(accountNumber, username);
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Withdrawal amount must be greater than zero.");
        }
        Account account = getAccount(accountNumber);
        BigDecimal newBalance = account.getBalance().subtract(amount);
        if (newBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Insufficient funds.");
        }
        account.setBalance(newBalance);
        transactionService.withdraw(accountNumber, amount);
        accountRepository.save(account);
       
    }

    @Transactional // Ensure that both account updates succeed or fail together
    public void transfer(String username, TransferRequest request) {
        String accountNumber1 = request.getFromAccountNumber();
        verifyOwnership(accountNumber1, username);
        
        // Check if account exists, ownership not required for destination account
        String accountNumber2 = request.getToAccountNumber();
        findAccount(accountNumber2); 
        
        BigDecimal amount = request.getAmount();
        if (accountNumber1.equals(accountNumber2)) {
            throw new BadRequestException("Source and destination accounts must be different.");
        }

        Account account1 = getAccount(accountNumber1);
        Account account2 = getAccount(accountNumber2);

        BigDecimal newBalance1 = account1.getBalance().add(request.getAmount());
        if (newBalance1.compareTo(BigDecimal.ZERO) < 0) {
            throw new BadRequestException("Insufficient funds in the source account.");
        }
        
        account1.setBalance(newBalance1);
        accountRepository.save(account1);

        account2.setBalance(account2.getBalance().add(amount));
        accountRepository.save(account2);
        transactionService.transfer(accountNumber1, accountNumber2, amount);

    }

    ///---------------------------------------- User Account Methods -----------------------------------------

    public List<AccountResponse> getUserAccounts(String username) {
        if (!userService.checkforUserName(username)) {
            throw new ResourceNotFoundException("User not found.");
        }
        return accountRepository.findByUsername(username).stream().map(AccountResponse::new).toList();
    }
    
    @Transactional
    public void deleteUserAccounts(String username) {
        if (!userService.checkforUserName(username)) {
            throw new ResourceNotFoundException("User not found.");
        }
        List<Account> accounts = accountRepository.findByUsername(username);
        accounts.forEach(account -> 
            transactionService.deleteAccountTransactions(account.getAccountNumber())
        );
        accountRepository.deleteAllByUsername(username);
    }
    

}
