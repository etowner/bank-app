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
import java.util.Random;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    private void verifyOwnership(int accountNumber, String username) {
        Account account = getAccount(accountNumber);
        if (!account.getUsername().equals(username)) {
            throw new AccessDeniedException("You do not own this account.");
        }
    }
    
    private boolean validate(String username, String type) {
        return username != null && !username.isBlank()
            && type != null && !type.isBlank();
    }

    private Account findAccount(int accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber).orElseThrow(() -> new ResourceNotFoundException("Account not found."));
    }
    
    // ---------------------------------------- Main Methods -----------------------------------------
    public void newAccount(String username, String type) {
        if (!validate(username, type)) {
            throw new BadRequestException("Account username and type are required.");
        }
       
        if (!userService.checkforUserName(username)) {
            throw new ResourceNotFoundException("User not found.");
        }

        try {
            int possAID = random.nextInt(1000, 9000); // Need to update this
            Account account = new Account(username, possAID, type);
            accountRepository.insert(account);
            mongoTemplate.update(User.class)
                .matching(Criteria.where("username").is(account.getUsername()))
                .apply(new Update().push("accounts").value(account))
                .first();
        } catch (DuplicateKeyException e) {
            throw new BadRequestException("Account creation failed, please try again.");
        }
    }

    private Account getAccount(int accountNumber) {
        return findAccount(accountNumber);
    }

    public AccountResponse getAccountResponse(int accountNumber) { 
        Account account = getAccount(accountNumber);
        return new AccountResponse(account);
    }

    public void deleteAccount(String username, int accountNumber) {
        verifyOwnership(accountNumber, username);
        Account account = getAccount(accountNumber);
        accountRepository.delete(account);
    }

    //---------------------------------------- Transaction Methods -----------------------------------------
    public void depositAmount(String username, int accountNumber, double amount) {
        verifyOwnership(accountNumber, username);
        if (amount <= 0) {
            throw new BadRequestException("Deposit amount must be greater than zero.");
        }
        Account account = getAccount(accountNumber);

        account.setBalance(account.getBalance() + amount);
        transactionService.deposit(accountNumber, amount);
        accountRepository.save(account);
    }

    public boolean withdrawAmount(String username, int accountNumber, double amount) {
        verifyOwnership(accountNumber, username);
        if (amount <= 0) {
            throw new BadRequestException("Withdrawal amount must be greater than zero.");
        }
        Account account = getAccount(accountNumber);
        double newBalance = account.getBalance() - amount;
        if (newBalance < 0) {
            throw new BadRequestException("Insufficient funds.");
        }
        transactionService.withdraw(accountNumber, amount);
        accountRepository.save(account);
        return true;
    }

    @Transactional // Ensure that both account updates succeed or fail together
    public void transfer(String username, TransferRequest request) {
        int accountNumber1 = request.getFromAccountNumber();
        verifyOwnership(accountNumber1, username);
        
        // Check if account exists, ownership not required for destination account
        int accountNumber2 = request.getToAccountNumber();
        findAccount(accountNumber2); 
        
        double amount = request.getAmount();
        if (accountNumber1 == accountNumber2) {
            throw new BadRequestException("Source and destination accounts must be different.");
        }

        Account account1 = getAccount(accountNumber1);
        Account account2 = getAccount(accountNumber2);

        double newBalance1 = account1.getBalance() - amount;
        if (newBalance1 < 0) {
            throw new BadRequestException("Insufficient funds in the source account.");
        }
        
        account1.setBalance(newBalance1);
        accountRepository.save(account1);

        

        account2.setBalance(account2.getBalance() + amount);
        accountRepository.save(account2);

        throw new RuntimeException("Simulated failure");
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
