package com.app.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.User;
import com.app.bank.repo.UserRepository;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean validateUser(User user){
        if (user == null || user.getUserID() == null || user.getUserID().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            return false;
        }
        return true;
    }

    public void newUser(User user) {
        if (!validateUser(user)) {
            throw new BadRequestException("UserID and password are required.");
        }
        if (checkforUser(user.getUserID())) {
            throw new BadRequestException("A user with this userID already exists.");
        }
        User encodedUser = new User(user.getUserID(), passwordEncoder.encode(user.getPassword()));
        userRepository.insert(encodedUser);
    }

    public boolean checkforUser(User user) {
        return user != null && checkforUser(user.getUserID());
    }

    public boolean checkforUser(String userID) {
        if (userID == null || userID.isBlank()) {
            return false;
        }
        return userRepository.getUserByUserID(userID).isPresent();
    }

    public boolean checkforUserPassword(User user) {
         if (!validateUser(user)){
            return false;
        }
        Optional<User> existingUser = userRepository.getUserByUserID(user.getUserID());
        return existingUser.filter(User -> passwordEncoder.matches(user.getPassword(), User.getPassword())).isPresent();
    }

    public Optional<User> getUser(String userID) {
        if (userID == null || userID.isBlank()) {
            return Optional.empty();
        }
        return userRepository.getUserByUserID(userID);
    }

    public void deleteUser(String userID) {
        Optional<User> user = getUser(userID);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found.");
        }
        userRepository.delete(user.get());
    }

    

}
