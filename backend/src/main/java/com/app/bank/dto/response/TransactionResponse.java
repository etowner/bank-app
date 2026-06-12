package com.app.bank.dto.response;

import java.math.BigDecimal;
import java.time.Instant;

import com.app.bank.model.TransactionType;
import com.app.bank.model.Transaction;

public class TransactionResponse {

    private String id;
    private TransactionType type;
    private BigDecimal amount;
    private Instant timestamp;
    private String counterparty;
    
    public TransactionResponse(Transaction transaction) {
        this.id = transaction.getId();
        this.type = transaction.getType();
        this.amount = transaction.getAmount();
        this.counterparty = transaction.getCounterparty();
        this.timestamp = transaction.getTimestamp();
    }

    public String getId() {
        return id;
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


}
