package com.app.bank.model;

import java.math.BigDecimal;

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
    private BigDecimal balance;

    public Account() {}
    
    public Account(String username, String accountNumber, String type) {
        this.username = username;
        this.type = type;
        this.accountNumber = accountNumber;
        this.balance = BigDecimal.ZERO;
    }

    public String getId() {
        return id.toHexString();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getType() {
        return type;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

}
