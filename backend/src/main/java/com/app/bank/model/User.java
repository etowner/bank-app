package com.app.bank.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Document(collection = "Users")
public class User implements UserDetails {

    @Id
    ObjectId id;
    
    private String userID;
    private String password;

    @Indexed(unique = true)
    private String email;

    @DocumentReference
    private List<Account> accounts;

    public User(String userID, String password, String email) {
        this.userID = userID;
        this.password = password;
        this.email = email;
        accounts = new ArrayList<>();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public List<Account> getAccountList() {
        return accounts;
    }

    public int getNumOfAccounts() {
        return accounts.size();
    }

    @Override
    public String getUsername() {
        return userID;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
