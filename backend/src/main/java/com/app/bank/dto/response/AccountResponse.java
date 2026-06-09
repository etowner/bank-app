package com.app.bank.dto.response;

import com.app.bank.model.Account;
import java.math.BigDecimal;

public class AccountResponse {
    
    private String accountNumber;
    private String type;
    private BigDecimal balance;

    public AccountResponse(Account account) {
        this.accountNumber = account.getAccountNumber();
        this.type = account.getType();
        this.balance = account.getBalance();
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


}
