package com.app.bank.service;

import org.springframework.stereotype.Service;

import com.app.bank.model.Transaction;
import com.app.bank.model.TransactionType;
import com.app.bank.repo.TransactionRepository;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }
    
    public void newTransaction(String accountNumber, TransactionType type, double amount, String counterparty) {
        try {
        Transaction transaction = new Transaction(accountNumber, type, amount, counterparty);
        transactionRepository.save(transaction);
        } catch (Exception e) {
            throw new RuntimeException("Failed to record transaction.", e);
        }
    }

    public void deposit(String accountNumber, double amount) {
        newTransaction(accountNumber, TransactionType.DEPOSIT, amount, null);
    }

    public void withdraw(String accountNumber, double amount) {
        newTransaction(accountNumber, TransactionType.WITHDRAWAL, amount, null);
    }

    public void transfer(String accountNumber1, String accountNumber2, double amount) {
        newTransaction(accountNumber1, TransactionType.TRANSFER_OUT, amount, String.valueOf(accountNumber2));
        newTransaction(accountNumber2, TransactionType.TRANSFER_IN, amount, String.valueOf(accountNumber1));
    }

    public void deleteAccountTransactions(String accountNumber) {
        transactionRepository.deleteAllByAccountNumber(accountNumber);
    }
}
