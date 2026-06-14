package com.app.bank.api;

import com.app.bank.dto.request.*;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.exception.BadRequestException;
import com.app.bank.dto.response.UserResponse;
import com.app.bank.security.UserPrincipal;
import com.app.bank.service.UserService;
import com.app.bank.service.ManagementService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;


@RequestMapping("api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final ManagementService managementService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager, ManagementService managementService) {
        this.userService = userService;
        this.managementService = managementService;
        this.authenticationManager = authenticationManager;
    }

    private void storeAuthentication(Authentication authentication, HttpServletRequest request) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);
        SecurityContextHolder.setContext(context);
        request.getSession(true).setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, context);
    }

    @GetMapping
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        UserResponse response = userService.getUser(principal.getUsername());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    public ResponseEntity<String> createAccount(@Valid @RequestBody RegisterRequest user, HttpServletRequest request) {
        try {
            userService.register(user);
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            storeAuthentication(authentication, request);
            return ResponseEntity.ok("Account created successfully.");
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while creating the account.");
        } 
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@Valid @RequestBody LoginRequest user, HttpServletRequest request) {
        try {
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
            storeAuthentication(authentication, request);
            return ResponseEntity.ok("Logged in");
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password.");
        }
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserPrincipal principal, 
        @Valid @RequestBody ChangePasswordRequest request) {
        try {
            userService.changePassword(principal.getUsername(), request.getCurrentPassword(), request.getNewPassword());
            return ResponseEntity.ok("Password updated successfully");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An error occurred while updating the password.");
        }
    }

    @PutMapping("/change-username")
    public ResponseEntity<String> changeUsername(@AuthenticationPrincipal UserPrincipal principal, 
        @Valid @RequestBody ChangeUsernameRequest request, HttpServletRequest HttpServletRequest) {
        try {
            String currentUsername = principal.getUsername();
            managementService.changeUsername(currentUsername, request);
            Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getNewUsername(), request.getCurrentPassword()));
            storeAuthentication(authentication, HttpServletRequest);
            return ResponseEntity.ok("Username updated successfully. Please log in again with your new username.");
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(e.getMessage());
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserPrincipal principal) {
        try {
            String username = principal.getUsername();
            managementService.deleteUser(username);
            return ResponseEntity.ok("Account deleted successfully");
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
