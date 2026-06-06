package com.app.bank.service;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.model.User;
import com.app.bank.repo.AccountRepository;
import com.mongodb.DuplicateKeyException;
import java.lang.Exception;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
public class AccountService {

    Random random = new Random();

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    // ---------------------------------------- Verify Methods -----------------------------------------
    public void verifyOwnership(int accountNumber, String username) throws Exception {
        Account account = getAccount(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account not found."));
        if (!account.getUsername().equals(username)) {
            throw new AccessDeniedException("You do not own this account.");
        }
    }
    
    private boolean validateAccount(Account account) {
        return account != null
            && account.getUsername() != null && !account.getUsername().isBlank()
            && account.getType() != null && !account.getType().isBlank();
    }
    
    // ---------------------------------------- Main Methods -----------------------------------------
    public void newAccount(Account account) {
        if (!validateAccount(account)) {
            throw new BadRequestException("Account username and type are required.");
        }
        if (!userService.checkforUserName(account.getUsername())) {
            throw new ResourceNotFoundException("User not found.");
        }
        try {
            int possAID = random.nextInt(1000, 9000);
            account = new Account(account.getUsername(), possAID, account.getType());
            accountRepository.insert(account);
            mongoTemplate.update(User.class)
                .matching(Criteria.where("username").is(account.getUsername()))
                .apply(new Update().push("accounts").value(account))
                .first();
        } catch (DuplicateKeyException e) {
            throw new BadRequestException("Account creation failed, please try again.");
        }
    }

    public Optional<Account> getAccount(int accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber);
    }

    public void deleteAccount(int accountNumber) {
        Optional<Account> account = getAccount(accountNumber);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Account not found.");
        }
        accountRepository.delete(account.get());
    }

    //---------------------------------------- Transaction Methods -----------------------------------------
    public void depositAmount(int accountNumber, double amount) {
        if (amount <= 0) {
            throw new BadRequestException("Deposit amount must be greater than zero.");
        }
        Account account = getAccount(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account not found."));

        account.setBalance(account.getBalance() + amount);
        transactionService.deposit(accountNumber, amount);
        accountRepository.save(account);
    }

    public boolean withdrawAmount(int accountNumber, double amount) {
        if (amount <= 0) {
            throw new BadRequestException("Withdrawal amount must be greater than zero.");
        }
        Account account = getAccount(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account not found."));
        double newBalance = account.getBalance() - amount;
        if (newBalance < 0) {
            throw new BadRequestException("Insufficient funds.");
        }
        transactionService.withdraw(accountNumber, amount);
        accountRepository.save(account);
        return true;
    }

    public void transfer(int accountNumber1, int accountNumber2, double amount) {
        if (accountNumber1 == accountNumber2) {
            throw new BadRequestException("Source and destination accounts must be different.");
        }
        if (amount <= 0) {
            throw new BadRequestException("Transfer amount must be greater than zero.");
        }
        Account account1 = getAccount(accountNumber1).orElseThrow(() -> new ResourceNotFoundException("Source account not found."));
        Account account2 = getAccount(accountNumber2).orElseThrow(() -> new ResourceNotFoundException("Destination account not found."));
        
        double newBalance1 = account1.getBalance() - amount;
        if (newBalance1 < 0) {
            throw new BadRequestException("Insufficient funds in the source account.");
        }
        account1.setBalance(newBalance1);
        account2.setBalance(account2.getBalance() + amount);
        accountRepository.save(account1);
        accountRepository.save(account2);
    }

    ///---------------------------------------- User Account Methods -----------------------------------------

    public List<Account> getUserAccounts(String username) {
    if (!userService.checkforUserName(username)) {
        throw new ResourceNotFoundException("User not found.");
    }
    return accountRepository.findByUsername(username);
    }

    public void deleteUserAccounts(String username) {
        if (!userService.checkforUserName(username)) {
            throw new ResourceNotFoundException("User not found.");
        }
        accountRepository.deleteAllByUsername(username);
    }
    

}
