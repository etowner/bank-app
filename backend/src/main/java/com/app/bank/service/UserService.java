package com.app.bank.service;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.User;
import com.app.bank.repo.UserRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // For testing purposes only - returns all users in the database
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Helper method to validate user input: userID and password must be non-null and non-blank
    public boolean validateUser(User user){
        return user != null 
            && user.getUserID() != null && !user.getUserID().isBlank()
            && user.getPassword() != null && !user.getPassword().isBlank();
    }

    // Checks if a user with the given userID exists in the database
    public boolean checkforUser(String userID) {
        if (userID == null || userID.isBlank()) return false;  
        return userRepository.findByUserID(userID).isPresent();
    }

    // Overloaded method to check if a User object exists in the database based on its userID
    public boolean checkforUser(User user) {
        return user != null && checkforUser(user.getUserID());
    }

    public void newUser(User user) {
        // Checks for null or blank userID/password
        if (!validateUser(user)) { 
            throw new BadRequestException("UserID and password are required.");
        }
        // Checks if userID already exists
        if (checkforUser(user.getUserID())) { 
            throw new BadRequestException("A user with this userID already exists.");
        }
        // Create new User with encoded password and save to repository
        User encodedUser = new User(user.getUserID(), passwordEncoder.encode(user.getPassword())); 
        userRepository.insert(encodedUser);
    }

    // Checks if the provided password matches the stored hashed password
    public boolean checkforUserPassword(User user) {
        if (!validateUser(user)) return false;
        Optional<User> existingUser = userRepository.findByUserID(user.getUserID());
        return existingUser.filter(User -> passwordEncoder.matches(user.getPassword(), User.getPassword())).isPresent();
    }

    // Retrieves a User by userID with all accounts
    public Optional<User> getUser(String userID) {
        if (userID == null || userID.isBlank()) return Optional.empty();
        return userRepository.findWithAccountsByUserID(userID);
    }

    // Retrieves a User by email with all accounts
    // public Optional<User> getUserByEmail(String email) {
    //     if (email == null || email.isBlank()) return Optional.empty();
    //     return userRepository.findByEmail(email);
    // }

    public void changePassword(String userID, String currentPassword, String newPassword) {
        User user = userRepository.findByUserID(userID).orElseThrow();

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    public void changeUserID(String currentUserID, String currentPassword, String newUserID) {
        if (newUserID == null || newUserID.isBlank()) {
            throw new BadRequestException("New userID cannot be blank.");
        }
        
        User user = userRepository.findByUserID(currentUserID).orElseThrow(
            () -> new ResourceNotFoundException("User not found.")
        );

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }

        if (checkforUser(newUserID)) {
            throw new BadRequestException("UserID already exists.");
        }

        user.setUserID(newUserID);
        userRepository.save(user);
    }

    public void deleteUser(String userID) {
        Optional<User> user = getUser(userID);
        if (user.isEmpty()) throw new ResourceNotFoundException("User not found.");
        userRepository.delete(user.get());
    }

    

}
