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
    private UserService userService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public void verifyOwnership(int accountID, String userID) throws Exception {
        Account account = getAccount(accountID).orElseThrow(() -> new ResourceNotFoundException("Account not found."));
        if (!account.getUserID().equals(userID)) {
            throw new AccessDeniedException("You do not own this account.");
        }
    }
    
    public List<Account> getUserAccounts(String userID) {
        if (!userService.checkforUser(userID)) {
            throw new ResourceNotFoundException("User not found.");
        }
        return accountRepository.findByUserID(userID);
    }

    public Optional<Account> getAccount(int accountID) {
        return accountRepository.findByAccountID(accountID);
    }

    private boolean validateAccount(Account account) {
        return account != null
            && account.getUserID() != null && !account.getUserID().isBlank()
            && account.getType() != null && !account.getType().isBlank();
    }
    
    public void newAccount(Account account) {
        if (!validateAccount(account)) {
            throw new BadRequestException("Account userID and type are required.");
        }
        if (!userService.checkforUser(account.getUserID())) {
            throw new ResourceNotFoundException("User not found.");
        }
        try {
            int possAID = random.nextInt(1000, 9000);
            account = new Account(account.getUserID(), possAID, account.getType());
            accountRepository.insert(account);
            mongoTemplate.update(User.class)
                .matching(Criteria.where("userID").is(account.getUserID()))
                .apply(new Update().push("accounts").value(account))
                .first();
        } catch (DuplicateKeyException e) {
            throw new BadRequestException("Account creation failed, please try again.");
        }
    }

    public void deleteAccount(int accountID) {
        Optional<Account> account = getAccount(accountID);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Account not found.");
        }
        accountRepository.delete(account.get());
    }

    public void deleteUserAccounts(String userID) {
        if (!userService.checkforUser(userID)) {
            throw new ResourceNotFoundException("User not found.");
        }
        accountRepository.deleteAllByUserID(userID);
    }

    public void depositAmount(int accountID, double amount) {
        if (amount <= 0) {
            throw new BadRequestException("Deposit amount must be greater than zero.");
        }
        Account account = getAccount(accountID)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found."));
        account.deposit(amount);
        accountRepository.save(account);
    }

    public boolean withdrawAmount(int accountID, double amount) {
        if (amount <= 0) {
            throw new BadRequestException("Withdrawal amount must be greater than zero.");
        }
        Account account = getAccount(accountID).orElseThrow(() -> new ResourceNotFoundException("Account not found."));
        if (!account.withdraw(amount)) {
            throw new BadRequestException("Insufficient funds.");
        }
        accountRepository.save(account);
        return true;
    }

    public void transfer(int accountID1, int accountID2, double amount) {
        if (accountID1 == accountID2) {
            throw new BadRequestException("Source and destination accounts must be different.");
        }
        if (amount <= 0) {
            throw new BadRequestException("Transfer amount must be greater than zero.");
        }
        Account account1 = getAccount(accountID1)
            .orElseThrow(() -> new ResourceNotFoundException("Source account not found."));
        Account account2 = getAccount(accountID2)
            .orElseThrow(() -> new ResourceNotFoundException("Destination account not found."));
        if (!account1.withdraw(amount)) {
            throw new BadRequestException("Insufficient funds in the source account.");
        }
        account2.deposit(amount);
        accountRepository.save(account1);
        accountRepository.save(account2);
       
    }

    

}
