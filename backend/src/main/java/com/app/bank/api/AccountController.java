package com.app.bank.api;

import com.app.bank.dto.request.*;
import com.app.bank.dto.response.AccountResponse;
import com.app.bank.security.UserPrincipal;
import com.app.bank.service.AccountService;
import jakarta.validation.Valid;
import java.math.BigDecimal;
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
        return ResponseEntity.ok(accountService.getAccountResponse(accountNumber, principal.getUsername()));
    }

    @PostMapping(path = "/open/{type}")
    public ResponseEntity<String> openAccount(@PathVariable String type,
            @AuthenticationPrincipal UserPrincipal principal) {
        accountService.newAccount(principal.getUsername(), type);
        return ResponseEntity.ok("Account opened successfully.");
    }

    @PostMapping(path = "/{accountNumber}/deposit")
    public ResponseEntity<AccountResponse> deposit(@PathVariable String accountNumber,
            @RequestBody BigDecimal amount,
            @AuthenticationPrincipal UserPrincipal principal) {
        accountService.depositAmount(principal.getUsername(), accountNumber, amount);
        return ResponseEntity.ok(accountService.getAccountResponse(accountNumber, principal.getUsername()));
    }

    @PostMapping(path = "/{accountNumber}/withdraw")
    public ResponseEntity<AccountResponse> withdraw(@PathVariable String accountNumber,
            @RequestBody BigDecimal amount,
            @AuthenticationPrincipal UserPrincipal principal) {
        accountService.withdrawAmount(principal.getUsername(), accountNumber, amount);
        return ResponseEntity.ok(accountService.getAccountResponse(accountNumber, principal.getUsername()));
    }

    @PostMapping(path = "/transfer")
    public ResponseEntity<String> transfer(@Valid @RequestBody TransferRequest request,
            @AuthenticationPrincipal UserPrincipal principal) {
        accountService.transfer(principal.getUsername(), request);
        return ResponseEntity.ok("Transfer successful.");
    }

    @DeleteMapping(path = "/{accountNumber}/close")
    public ResponseEntity<String> deleteAccount(@PathVariable String accountNumber,
            @AuthenticationPrincipal UserPrincipal principal) {
        accountService.deleteAccount(principal.getUsername(), accountNumber);
        return ResponseEntity.ok("Account closed successfully.");
    }

    @DeleteMapping(path = "/close-all")
    public ResponseEntity<String> deleteAllUserAccounts(@AuthenticationPrincipal UserPrincipal principal) {
        accountService.deleteUserAccounts(principal.getUsername());
        return ResponseEntity.ok("Accounts closed successfully.");
    }

}
