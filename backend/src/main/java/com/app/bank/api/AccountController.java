package com.app.bank.api;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.service.AccountService;
import com.app.bank.dto.request.*;
import com.app.bank.dto.response.AccountResponse;
import com.app.bank.security.UserPrincipal;
import jakarta.validation.Valid;
import java.math.BigDecimal;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/account")
@RestController
public class AccountController {
    
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @GetMapping(path = "/{accountNumber}")
    public ResponseEntity<AccountResponse> getUserAccount(@PathVariable String accountNumber, 
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            return ResponseEntity.ok(accountService.getAccountResponse(accountNumber));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
    }

    @PostMapping(path = "/open/{type}") // or PostMapping with a "type" parameter in the body
    public ResponseEntity<String> openAccount(@PathVariable String type, @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.newAccount(principal.getUsername(), type);
            return ResponseEntity.ok("Account opened successfully.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping(path = "/{accountNumber}/deposit")
    public ResponseEntity<?> deposit(@PathVariable String accountNumber, @RequestBody BigDecimal amount,
            @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.depositAmount(principal.getUsername(), accountNumber, amount);
            return ResponseEntity.ok(accountService.getAccountResponse(accountNumber));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }  catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @PostMapping(path = "/{accountNumber}/withdraw")
    public ResponseEntity<?> withdraw(@PathVariable String accountNumber,
            @RequestBody BigDecimal amount, @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.withdrawAmount(principal.getUsername(), accountNumber, amount);
            return ResponseEntity.ok(accountService.getAccountResponse(accountNumber));
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/{accountNumber}/close")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber,
           @AuthenticationPrincipal UserPrincipal principal) {
        try {
            accountService.deleteAccount(principal.getUsername(), accountNumber);
            return ResponseEntity.ok("Account closed successfully.");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
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
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
    

}
