package com.app.bank.dto.response;

import com.app.bank.model.Account;

public class AccountResponse {
    private int accountNumber;
    private String type;
    private double balance;

    public AccountResponse(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.type = account.getType();
        this.balance = account.getBalance();
    }

    public int getAccountNumber() {
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
