package com.app.bank.api;

import com.app.bank.dto.response.TransactionResponse;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.security.UserPrincipal;
import com.app.bank.service.TransactionService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/account")
@RestController
public class TransactionController {

    private final TransactionService transactionService;
    
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }
    
    @GetMapping("/{accountNumber}/transactions")
    public ResponseEntity<?> getTransactions(@PathVariable String accountNumber, @AuthenticationPrincipal UserPrincipal principal) {
        try {
            List<TransactionResponse> transactions = transactionService.getAccountTransactions(accountNumber, principal.getUsername());
            return ResponseEntity.ok(transactions);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account or transactions not found.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }
}
