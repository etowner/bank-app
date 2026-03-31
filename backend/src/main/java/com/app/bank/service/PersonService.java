package com.app.bank.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Person;
import com.app.bank.repo.PersonRepository;

@Service
public class PersonService {

    @Autowired
    private PersonRepository userRepository;

    public List<Person> getAllUsers() {
        return userRepository.findAll();
    }

    public boolean validateUser(Person user){
        if (user == null || user.getUserID() == null || user.getUserID().isBlank()
                || user.getPassword() == null || user.getPassword().isBlank()) {
            return false;
        }
        return true;
    }

    public void newUser(Person user) {
        if (!validateUser(user)) {
            throw new BadRequestException("UserID and password are required.");
        }
        if (checkforUser(user.getUserID())) {
            throw new BadRequestException("A user with this userID already exists.");
        }
        userRepository.insert(user);
    }

    public boolean checkforUser(Person user) {
        return user != null && checkforUser(user.getUserID());
    }

    public boolean checkforUser(String userID) {
        if (userID == null || userID.isBlank()) {
            return false;
        }
        return userRepository.getUserByUserID(userID).isPresent();
    }

    public boolean checkforUserPassword(Person user) {
         if (!validateUser(user)){
            return false;
        }
        Optional<Person> existingUser = userRepository.getUserByUserID(user.getUserID());
        return existingUser.filter(person -> person.getPassword().equals(user.getPassword())).isPresent();
    }

    public Optional<Person> getUser(String userID) {
        if (userID == null || userID.isBlank()) {
            return Optional.empty();
        }
        return userRepository.getUserByUserID(userID);
    }

    public void deleteUser(String userID) {
        Optional<Person> user = getUser(userID);
        if (user.isEmpty()) {
            throw new ResourceNotFoundException("User not found.");
        }
        userRepository.delete(user.get());
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }

}
