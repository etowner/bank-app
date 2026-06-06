package com.app.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bank.model.Transaction;
import com.app.bank.model.TransactionType;
import com.app.bank.repo.TransactionRepository;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;
    
    public void newTransaction(int accountId, TransactionType type, double amount, String counterparty) {
        Transaction transaction = new Transaction(accountId, type, amount, counterparty);
        transactionRepository.save(transaction);
    }

    public void deposit(int accountId, double amount) {
        newTransaction(accountId, TransactionType.DEPOSIT, amount, null);
    }

    public void withdraw(int accountId, double amount) {
        newTransaction(accountId, TransactionType.WITHDRAWAL, amount, null);
    }

    public void transfer(int accountId1, int accountId2, double amount) {
        newTransaction(accountId1, TransactionType.TRANSFER, amount, String.valueOf(accountId2));
        newTransaction(accountId2, TransactionType.TRANSFER, amount, String.valueOf(accountId1));
    }
}
