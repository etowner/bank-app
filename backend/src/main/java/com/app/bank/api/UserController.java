package com.app.bank.api;

import com.app.bank.dto.request.*;
import com.app.bank.dto.response.UserResponse;
import com.app.bank.security.UserPrincipal;
import com.app.bank.service.ManagementService;
import com.app.bank.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/v1/user")
@RestController
public class UserController {

    private final UserService userService;
    private final ManagementService managementService;
    private final AuthenticationManager authenticationManager;

    public UserController(UserService userService, AuthenticationManager authenticationManager,
            ManagementService managementService) {
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
        userService.register(user);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        storeAuthentication(authentication, request);
        return ResponseEntity.ok("Account created successfully.");
    }

    @PostMapping("/login")
    public ResponseEntity<String> logIn(@Valid @RequestBody LoginRequest user, HttpServletRequest request) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        storeAuthentication(authentication, request);
        return ResponseEntity.ok("Logged in");
    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangePasswordRequest request) {
        userService.changePassword(principal.getUsername(), request.getCurrentPassword(), request.getNewPassword());
        return ResponseEntity.ok("Password updated successfully");
    }

    @PutMapping("/change-username")
    public ResponseEntity<String> changeUsername(@AuthenticationPrincipal UserPrincipal principal,
            @Valid @RequestBody ChangeUsernameRequest request, HttpServletRequest httpServletRequest) {
        String currentUsername = principal.getUsername();
        managementService.changeUsername(currentUsername, request);
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(request.getNewUsername(), request.getCurrentPassword()));
        storeAuthentication(authentication, httpServletRequest);
        return ResponseEntity.ok("Username updated successfully. Please log in again with your new username.");
    }

    @DeleteMapping
    public ResponseEntity<String> deleteUser(@AuthenticationPrincipal UserPrincipal principal) {
        String username = principal.getUsername();
        managementService.deleteUser(username);
        return ResponseEntity.ok("Account deleted successfully");
    }

}
