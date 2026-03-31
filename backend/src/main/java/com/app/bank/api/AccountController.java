package com.app.bank.api;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.service.AccountService;


@CrossOrigin("*")
@RequestMapping("api/v1/user/{userID}")
@RestController
public class AccountController {
    @Autowired
    private AccountService accountServices;

    @GetMapping(path = "/accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@PathVariable("userID") String userID) {
        List<Account> userAccounts = accountServices.getUserAccounts(userID);
        return ResponseEntity.ok(userAccounts);
    }

    @GetMapping(path = "/{accountID}")
    public ResponseEntity<Account> getUserAccount(@PathVariable("accountID") int accountID) {
        return accountServices.getAccount(accountID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping(path = "/{type}")
    public ResponseEntity<String> openAccount(@PathVariable("userID") String userID,
            @PathVariable("type") String type) {
        if (type == null || type.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account type is required.");
        }
        try {
            accountServices.newAccount(new Account(userID, type));
            return ResponseEntity.ok("Account opened successfully.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid account request.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PutMapping(path = "{accountID}/deposit")
    public ResponseEntity<?> deposit(@PathVariable("accountID") int accountID, @RequestBody float amount) {
        try {
            accountServices.depositAmount(accountID, amount);
            return accountServices.getAccount(accountID)
                    .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid deposit request.");
        }
    }

    @PutMapping(path = "{accountID}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable("accountID") int accountID,
            @RequestBody float amount) {
        try {
            accountServices.withdrawAmount(accountID, amount);
            return accountServices.getAccount(accountID)
                    .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PutMapping(path = "{accountID1}/{accountID2}")
    public ResponseEntity<String> transfer(@PathVariable("accountID1") int accountID1,
            @PathVariable("accountID2") int accountID2,
            @RequestBody float amount) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount.");
        }
        try {
            accountServices.transfer(accountID1, accountID2, amount);
            return ResponseEntity.ok("Transfer successful.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{accountID}/close")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountID") int accountID) {
        try {
            accountServices.deleteAccount(accountID);
            return ResponseEntity.ok("Account closed successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/closeAll")
    public ResponseEntity<String> deleteAllUserAccounts(@PathVariable("userID") String userID) {
        try {
            accountServices.deleteUserAccounts(userID);
            return ResponseEntity.ok("Accounts closed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
