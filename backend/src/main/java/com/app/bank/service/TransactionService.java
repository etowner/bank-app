package com.app.bank.service;

import com.app.bank.dto.response.TransactionResponse;
import com.app.bank.model.Transaction;
import com.app.bank.model.TransactionType;
import com.app.bank.repo.TransactionRepository;
import com.app.bank.security.OwnershipService;
import java.math.BigDecimal;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final OwnershipService ownershipService;

    public TransactionService(TransactionRepository transactionRepository, OwnershipService ownershipService) {
        this.transactionRepository = transactionRepository;
        this.ownershipService = ownershipService;
    }
    
    public void newTransaction(String accountNumber, TransactionType type, BigDecimal amount, String counterparty) {
        try {
        Transaction transaction = new Transaction(accountNumber, type, amount, counterparty);
        transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to record transaction.", e);
        }
    }

    public List<TransactionResponse> getAccountTransactions(String accountNumber, String username) {
        ownershipService.verifyAccountOwnership(accountNumber, username);
        return transactionRepository.findByAccountNumber(accountNumber).stream().map(TransactionResponse::new).toList();
    }

    public void deposit(String accountNumber, BigDecimal amount) {
        newTransaction(accountNumber, TransactionType.DEPOSIT, amount, null);
    }

    public void withdraw(String accountNumber, BigDecimal amount) {
        newTransaction(accountNumber, TransactionType.WITHDRAWAL, amount, null);
    }

    public void transfer(String accountNumber1, String accountNumber2, BigDecimal amount) {
        newTransaction(accountNumber1, TransactionType.TRANSFER_TO, amount, String.valueOf(accountNumber2));
        newTransaction(accountNumber2, TransactionType.TRANSFER_FROM, amount, String.valueOf(accountNumber1));
    }

    public void deleteAccountTransactions(String accountNumber) {
        transactionRepository.deleteAllByAccountNumber(accountNumber);
    }
}
