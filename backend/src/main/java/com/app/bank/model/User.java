package com.app.bank.model;

import java.util.ArrayList;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

@Document(collection = "Users")
public class User {

    @Id
    ObjectId id;
    
    @Indexed(unique = true)
    private String userID;
    private String password;

    // private String email;

    @DocumentReference
    private List<Account> accounts;

    public User(String userID, String password) {
        this.userID = userID;
        this.password = password;
        // this.email = email;
        accounts = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
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
