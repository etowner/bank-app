package com.app.bank.api;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.service.AccountService;
import com.app.bank.dto.request.*;
import com.app.bank.dto.response.AccountResponse;
import com.app.bank.security.UserPrincipal;
import jakarta.validation.Valid;

import java.security.Principal;
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
    private AccountService accountService;

    @GetMapping(path = "/{accountNumber}")
    public ResponseEntity<AccountResponse> getUserAccount(@PathVariable("accountNumber") int accountNumber, 
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return ResponseEntity.ok(accountService.getAccountResponse(accountNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping(path = "open/{type}")
    public ResponseEntity<String> openAccount(@PathVariable("type") String type, @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.newAccount(principal.getUsername(), type);
            return ResponseEntity.ok("Account opened successfully.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid account request.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found.");
        }
    }

    @PostMapping(path = "{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable("accountNumber") int accountNumber, @RequestBody double amount,
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.depositAmount(principal.getUsername(), accountNumber, amount);
            return ResponseEntity.ok(accountService.getAccountResponse(accountNumber));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid deposit request.");
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping(path = "{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable("accountNumber") int accountNumber,
            @RequestBody double amount, @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.withdrawAmount(principal.getUsername(), accountNumber, amount);
            return ResponseEntity.ok(accountService.getAccountResponse(accountNumber));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid withdrawal request.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping(path = "/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest request,
        @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.transfer(principal.getUsername(), request);
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
           @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.deleteAccount(principal.getUsername(), accountNumber);
            return ResponseEntity.ok("Account closed successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }


    @DeleteMapping(path = "/close-all")
    public ResponseEntity<String> deleteAllUserAccounts(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.deleteUserAccounts(principal.getUsername());
            return ResponseEntity.ok("Accounts closed successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User accounts not found.");
        }
    }
    

}
