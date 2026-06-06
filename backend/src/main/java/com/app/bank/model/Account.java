package com.app.bank.model;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "accounts")
public class Account {
    @Id
    private ObjectId id;
    
    @Indexed
    private String username;
    private String type;

    @Indexed(unique = true)
    private int accountNumber;

    private double balance;

    public Account(String username, int accountNumber, String type) {
        this.username = username;
        this.type = type;
        this.accountNumber = accountNumber;
        this.balance = 0;
    }

    public String getUsername() {
        return username;
    }

    public String getType() {
        return type;
    }

    public int getAccountNumber() {
        return accountNumber;
    }
    
    public String getId() {
        return id.toHexString();
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    // public void addTrans(int transNum, Transaction trans) {
    //     transHistory.put(transNum, trans);
    // }

    // public void deposit(double amount) {
    //     balance += amount;
    //     transNum++;
    //     addTrans(transNum, new Transaction(TransactionType.DEPOSIT, amount));
    // }

    // public boolean withdraw(double amount) {
    //     if (amount <= balance) {
    //         balance -= amount;
    //         transNum++;
    //         addTrans(transNum, new Transaction(TransactionType.WITHDRAW, amount));
    //         return true;
    //     }
    //     return false;
    // }
}
