package com.app.bank.service;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.User;
import com.app.bank.repo.UserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.app.bank.dto.request.*;
import com.app.bank.dto.response.UserResponse;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ---------------------------------------- Helper Methods -----------------------------------------
    
    // Checks if a user with the given username exists in the database
    public boolean checkforUserName(String username) { 
        return userRepository.findByUsername(username).isPresent();
    }

    // Finds and returns a User by username, or throws ResourceNotFoundException if not found
    private User findUserByUsername(String username) {
        return userRepository.findWithAccountsByUsername(username)
            .orElseThrow(() -> new ResourceNotFoundException("User and accounts not found."));
    }

    // ---------------------------------------- Main Methods -----------------------------------------

    // Registers a new user after validating input and checking for existing username
    public void register(RegisterRequest request) {
        if (checkforUserName(request.getUsername())) { 
            throw new BadRequestException("A user with this username already exists.");
        }

        User encodedUser = new User(request.getUsername(), passwordEncoder.encode(request.getPassword()));
        userRepository.insert(encodedUser);
    }

    // Retrieves a User by username with all accounts and returns a UserResponse DTO
    public UserResponse getUser(String username) {
        User user = findUserByUsername(username);
        return new UserResponse(user);
    }


    public void changePassword(String username, String currentPassword, String newPassword) {
        User user = findUserByUsername(username);

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeUsername(String currentUsername, String currentPassword, String newUsername) {
        User user = findUserByUsername(currentUsername);
        
        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        if (checkforUserName(newUsername)) {
            throw new BadRequestException("Username already exists.");
        }

        user.setUsername(newUsername);
        userRepository.save(user);
    }

    public void deleteUser(String username) {
        User user = findUserByUsername(username);
        userRepository.delete(user);
    }

    

}
