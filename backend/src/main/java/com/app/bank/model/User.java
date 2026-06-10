package com.app.bank.model;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "users")
public class User {

    @Id
    private ObjectId id;
    
    @Indexed(unique = true)
    private String username;
    private String password;

    // private String email;
    private List<Account> accounts;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        // this.email = email;
        this.accounts = new ArrayList<>();
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

     public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    // public String getEmail() {
    //     return email;
    // }

    public List<Account> getAccountList() {
        return accounts;
    }

    public int getNumOfAccounts() {
        return accounts.size();
    }

   
}
