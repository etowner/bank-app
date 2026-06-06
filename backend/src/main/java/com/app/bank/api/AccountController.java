package com.app.bank.api;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.service.AccountService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/account")
@RestController
public class AccountController {
    
    @Autowired
    private AccountService accountServices;

    @GetMapping(path = "/my-accounts")
    public ResponseEntity<List<Account>> getUserAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        List<Account> userAccounts = accountServices.getUserAccounts(userDetails.getUsername());
        return ResponseEntity.ok(userAccounts);
    }

    @GetMapping(path = "/{accountNumber}")
    public ResponseEntity<Account> getUserAccount(@PathVariable("accountNumber") int accountNumber, 
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.verifyOwnership(accountNumber, userDetails.getUsername());
            return accountServices.getAccount(accountNumber)
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
            accountServices.newAccount(new Account(userDetails.getUsername(), 0, type));
            return ResponseEntity.ok("Account opened successfully.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid account request.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PutMapping(path = "{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable("accountNumber") int accountNumber, @RequestBody double amount,
            @AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.verifyOwnership(accountNumber, userDetails.getUsername());
            accountServices.depositAmount(accountNumber, amount);
            return accountServices.getAccount(accountNumber)
                    .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid deposit request.");
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping(path = "{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable("accountNumber") int accountNumber,
            @RequestBody double amount, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.verifyOwnership(accountNumber, userDetails.getUsername());
            accountServices.withdrawAmount(accountNumber, amount);
            return accountServices.getAccount(accountNumber)
                    .map(account -> new ResponseEntity<>(account, HttpStatus.OK))
                    .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid withdrawal request.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PutMapping(path = "{accountNumber1}/{accountNumber2}")
    public ResponseEntity<String> transfer(@PathVariable("accountNumber1") int accountNumber1,
            @PathVariable("accountNumber2") int accountNumber2,
            @RequestBody double amount, @AuthenticationPrincipal UserDetails userDetails) {
        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid amount.");
        }
        try {
            accountServices.verifyOwnership(accountNumber1, userDetails.getUsername());
            accountServices.verifyOwnership(accountNumber2, userDetails.getUsername());
            accountServices.transfer(accountNumber1, accountNumber2, amount);
            return ResponseEntity.ok("Transfer successful.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid transfer request.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "{accountNumber}/close")
    public ResponseEntity<String> deleteAccount(@PathVariable("accountNumber") int accountNumber,
           @AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.verifyOwnership(accountNumber, userDetails.getUsername());
            accountServices.deleteAccount(accountNumber);
            return ResponseEntity.ok("Account closed successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/closeAll")
    public ResponseEntity<String> deleteAllUserAccounts(@AuthenticationPrincipal UserDetails userDetails) {
        try {
            accountServices.deleteUserAccounts(userDetails.getUsername());
            return ResponseEntity.ok("Accounts closed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User accounts not found.");
        }
    }
    

}
