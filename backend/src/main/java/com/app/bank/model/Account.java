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
    
    @Indexed(unique = true)
    private String accountNumber;
    
    private String type;
    private double balance;

    public Account(String username, String accountNumber, String type) {
        this.username = username;
        this.type = type;
        this.accountNumber = accountNumber;
        this.balance = 0;
    }

    public String getId() {
        return id.toHexString();
    }

    public String getUsername() {
        return username;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

}
