package com.app.bank.service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.model.Person;
import com.app.bank.repo.AccountRepository;

@Service
public class AccountService {

    Random random = new Random();

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private PersonService personService;

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public List<Account> getUserAccounts(String userID) {
        if (!personService.checkforUser(userID)) {
            throw new ResourceNotFoundException("User not found.");
        }
        return accountRepository.getAccountsByUserID(userID);
    }

    public Optional<Account> getAccount(int accountID) {
        return accountRepository.getAccountByAccountID(accountID);
    }

    public Boolean isAccount(int accountID) {
        return accountRepository.getAccountByAccountID(accountID).isPresent();
    }

    public void newAccount(Account account) {
        if (account == null || account.getUserID() == null || account.getUserID().isBlank()
                || account.getType() == null || account.getType().isBlank()) {
            throw new BadRequestException("Account userID and type are required.");
        }
        if (!personService.checkforUser(account.getUserID())) {
            throw new ResourceNotFoundException("User not found.");
        }
        int possAID = random.nextInt(4000, 6000);
        while (Boolean.TRUE.equals(isAccount(possAID))) {
            possAID = random.nextInt(4000, 6000);
        }
        account.setAccountID(possAID);
        accountRepository.insert(account);
        String userID = account.getUserID();
        mongoTemplate.update(Person.class)
                .matching(Criteria.where("userID").is(userID))
                .apply(new Update().push("accounts").value(account))
                .first();
    }

    public void deleteAccount(int accountID) {
        Optional<Account> account = getAccount(accountID);
        if (account.isEmpty()) {
            throw new ResourceNotFoundException("Account not found.");
        }
        accountRepository.delete(account.get());
    }

    public void deleteUserAccounts(String userID) {
        if (!personService.checkforUser(userID)) {
            throw new ResourceNotFoundException("User not found.");
        }
        accountRepository.deleteAllAccountsByUserID(userID);
    }

    public void depositAmount(int accountID, float amount) {
        if (amount <= 0) {
            throw new BadRequestException("Deposit amount must be greater than zero.");
        }
        Account account = getAccount(accountID)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));
        account.deposit(amount);
        accountRepository.save(account);
    }

    public boolean withdrawAmount(int accountID, float amount) {
        if (amount <= 0) {
            throw new BadRequestException("Withdrawal amount must be greater than zero.");
        }
        Account account = getAccount(accountID)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found."));
        if (!account.withdraw(amount)) {
            throw new BadRequestException("Insufficient funds.");
        }
        accountRepository.save(account);
        return true;
    }

    public void transfer(int accountID1, int accountID2, float amount) {
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
