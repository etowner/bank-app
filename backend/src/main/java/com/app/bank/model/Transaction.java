package com.app.bank.model;

import java.math.BigDecimal;
import java.time.Instant;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "transactions")
public class Transaction {
    @Id
    private ObjectId id;

    private String accountNumber;
    
    private TransactionType type;
    private BigDecimal amount;
    private Instant timestamp;
   
    private String counterparty; // For transfers, the other account involved

    public Transaction() {}
    
    public Transaction(String accountNumber, TransactionType type, BigDecimal amount, String counterparty) {
        this.accountNumber = accountNumber;
        this.type = type;
        this.amount = amount;
        this.counterparty = counterparty;
        this.timestamp = Instant.now();
    }

    public String getId() {
        return id.toHexString();
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public TransactionType getType() {
        return type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public String getCounterparty() {
        return counterparty;
    }   

    @Override
    public String toString() {
        return type + " " + amount + " to " + counterparty;
    }
}
