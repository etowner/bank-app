package com.app.bank.api;

import java.util.*;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.app.bank.model.Person;
import com.app.bank.model.Account;
import com.app.bank.service.AccountService;
import com.app.bank.service.PersonService;

@CrossOrigin("*")
@RequestMapping("api/v1/user")
@RestController
public class PersonController {

    @Autowired
    private PersonService personService;

    @Autowired
    private AccountService accountService;

    private record PersonResponse(String userID, List<Account> accountList, int numOfAccounts) {
    }

    private final AuthenticationManager authenticationManager;

    public PersonController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    private void storeAuthentication(Authentication authentication, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

    private PersonResponse toPersonResponse(Person person) {
        return new PersonResponse(person.getUserID(), person.getAccountList(), person.getNumOfAccounts());
    }

    @GetMapping
    public ResponseEntity<PersonResponse> getCurrentUser(@AuthenticationPrincipal Object principal) {
        if (!(principal instanceof UserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        Optional<Person> user = personService.getUser(userDetails.getUsername());
        return user.map(this::toPersonResponse)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@RequestBody Person user, HttpServletRequest request) {
        if (!personService.validateUser(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserID and password are required.");
        }
        if (personService.checkforUser(user)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Account already exists.");
        }
        try {
            personService.newUser(user);
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUserID(), user.getPassword()));
            storeAuthentication(authentication, request);
            return ResponseEntity.ok("Account created successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the account.");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@RequestBody Person user, HttpServletRequest request) {
        if (!personService.validateUser(user)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("UserID and password are required.");
        }
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(user.getUserID(), user.getPassword()));
            storeAuthentication(authentication, request);
            return ResponseEntity.ok("Logged in");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Incorrect UserID or password.");
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal Object principal) {
        if (!(principal instanceof UserDetails userDetails)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        try {
            String userID = userDetails.getUsername();
            personService.deleteUser(userID);
            accountService.deleteUserAccounts(userID);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (Exception e) {
            if (e instanceof com.app.bank.exception.ResourceNotFoundException) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found.");
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred");
        }
    }

    

}
