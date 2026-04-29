package com.app.bank.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.app.bank.model.Person;
import com.app.bank.repo.PersonRepository;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    @Autowired
    private PersonRepository personRepository;

    @Override
    public UserDetails loadUserByUsername(String userID) throws UsernameNotFoundException {
        Person person = personRepository.getUserByUserID(userID)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userID));

        return User.withUsername(person.getUserID())
                .password(person.getPassword())
                .roles("USER")
                .build();
    }
}