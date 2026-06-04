package com.app.bank.model;

import java.util.HashMap;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Accounts")
public class Account {
    @Id
    private ObjectId id;
    
    @Indexed
    private String userID;
    private String type;

    @Indexed(unique = true)
    private int accountID;

    private double balance;
    private int transNum;

    private HashMap<Integer, Transaction> transHistory;

    public Account(String userID, int accountID, String type) {
        this.userID = userID;
        this.type = type;
        this.accountID = accountID;
        this.balance = 0;
        this.transHistory = new HashMap<>(); 
    }

    public String getUserID() {
        return userID;
    }

    public String getType() {
        return type;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public double getBalance() {
        return balance;
    }

    public HashMap<Integer, Transaction> getTransHistory() {
        return new HashMap<>(transHistory);
    }

    public void addTrans(int transNum, Transaction trans) {
        transHistory.put(transNum, trans);
    }

    public void deposit(double amount) {
        balance += amount;
        transNum++;
        addTrans(transNum, new Transaction(TransactionType.DEPOSIT, amount));
    }

    public boolean withdraw(double amount) {
        if (amount <= balance) {
            balance -= amount;
            transNum++;
            addTrans(transNum, new Transaction(TransactionType.WITHDRAW, amount));
            return true;
        }
        return false;
    }
}
