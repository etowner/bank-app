package com.app.bank.dto.response;

import java.util.List;
import com.app.bank.model.User;

public class UserResponse {
    private String username;
    private List<AccountResponse> accounts;
    private int numOfAccounts;
    
    public UserResponse(User user) {
        this.username = user.getUsername();
        this.accounts = user.getAccountList().stream()
            .map(AccountResponse::new)
            .toList();
        this.numOfAccounts = user.getAccountList().size();
    }

    public String getUsername() {
        return username;
    }

    public List<AccountResponse> getAccounts() {
        return accounts;
    }

    public int getNumOfAccounts() {
        return numOfAccounts;
    }

}
