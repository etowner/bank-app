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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.service.AccountService;


@CrossOrigin("*")
@RequestMapping("api/v1/account/")
@RestController
public class AccountController {
    
    @Autowired
    private AccountService accountServices;

    @GetMapping(path = "/my-accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        List<Account> userAccounts = accountServices.getUserAccounts(userDetails.getUsername());
        return ResponseEntity.ok(userAccounts);
    }

    @GetMapping(path = "/{accountID}")
    public ResponseEntity<Account> getUserAccount(@PathVariable("accountID") int accountID, 
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.verifyOwnership(accountID, userDetails.getUsername());
            return accountServices.getAccount(accountID)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping(path = "/{type}")
    public ResponseEntity<String> openAccount(@PathVariable("type") String type, @AuthenticationPrincipal UserDetails userDetails) {
        if (type == null || type.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account type is required.");
        }
        try {
            accountServices.newAccount(new Account(userDetails.getUsername(), type));
            return ResponseEntity.ok("Account opened successfully.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid account request.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PutMapping(path = "{accountID}/deposit")
    public ResponseEntity<?> deposit(@PathVariable("accountID") int accountID, @RequestBody float amount,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.verifyOwnership(accountID, userDetails.getUsername());
            accountServices.depositAmount(accountID, amount);
            return accountServices.getAccount(accountID)
                    .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid deposit request.");
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping(path = "{accountID}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable("accountID") int accountID,
            @RequestBody float amount, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.verifyOwnership(accountID, userDetails.getUsername());
            accountServices.withdrawAmount(accountID, amount);
            return accountServices.getAccount(accountID)
                    .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid withdrawal request.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping(path = "{accountID1}/{accountID2}")
    public ResponseEntity<String> transfer(@PathVariable("accountID1") int accountID1,
            @PathVariable("accountID2") int accountID2,
            @RequestBody float amount, @AuthenticationPrincipal UserDetails userDetails) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount.");
        }
        try {
            accountServices.verifyOwnership(accountID1, userDetails.getUsername());
            accountServices.verifyOwnership(accountID2, userDetails.getUsername());
            accountServices.transfer(accountID1, accountID2, amount);
            return ResponseEntity.ok("Transfer successful.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid transfer request.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{accountID}/close")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountID") int accountID,
          @RequestBody float amount, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.verifyOwnership(accountID, userDetails.getUsername());
            accountServices.deleteAccount(accountID);
            return ResponseEntity.ok("Account closed successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/closeAll")
    public ResponseEntity<String> deleteAllUserAccounts(@PathVariable("userID") String userID,
          @RequestBody float amount, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            if (!userID.equals(userDetails.getUsername())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Unauthorized.");
            }
            accountServices.deleteUserAccounts(userID);
            return ResponseEntity.ok("Accounts closed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User accounts not found.");
        }
    }
    

}
