package com.app.bank.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.app.bank.dto.request.ChangeUsernameRequest;
import com.app.bank.dto.request.RegisterRequest;
import com.app.bank.dto.response.UserResponse;
import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.User;
import com.app.bank.repo.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterRequest registerRequest;
    private static final String USERNAME = "testUser";
    private static final String RAW_PASSWORD = "testPass";
    private static final String ENCODED_PASSWORD = "encoded_testPass";

    @BeforeEach
    public void setUp() {
       registerRequest = new RegisterRequest(USERNAME, RAW_PASSWORD);
       when(passwordEncoder.encode(RAW_PASSWORD)).thenReturn(ENCODED_PASSWORD);
    }

    // Registration Tests

    @Test
    public void register_shouldInsertUser_whenValidUser(){
        when(userRepository.findByUsername(registerRequest.getUsername())).thenReturn(Optional.empty());

        userService.register(registerRequest);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).insert(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertNotNull(savedUser);
        assertEquals(USERNAME, savedUser.getUsername());
        assertEquals(ENCODED_PASSWORD, savedUser.getPassword());
    }

    @Test
    public void register_shouldThrowBadRequest_whenUserAlreadyExists() {
        User existingUser = new User(USERNAME, passwordEncoder.encode(RAW_PASSWORD));
        
        when(userRepository.findByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));
        BadRequestException exception = assertThrows(BadRequestException.class, () -> userService.register(registerRequest));
        
        assertEquals("A user with this username already exists.", exception.getMessage());
        verify(userRepository, never()).insert(existingUser);
    }

    // Get User Test

    @Test
    public void getUser_shouldReturnUserResponse_whenUserExists() {
        User existingUser = new User(USERNAME, passwordEncoder.encode(RAW_PASSWORD));
        
        when(userRepository.findWithAccountsByUsername(existingUser.getUsername())).thenReturn(Optional.of(existingUser));
        UserResponse userResponse = userService.getUser(existingUser.getUsername());
        
        assertNotNull(userResponse);
        assertEquals(existingUser.getUsername(), userResponse.getUsername());
    }

    // Change Username Test
    @Test
    public void changeUsername_shouldUpdateUsername_whenValid() {
        String newUsername = "newUsername";
        ChangeUsernameRequest request = new ChangeUsernameRequest(newUsername, RAW_PASSWORD);
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        
        userService.changeUsername(USERNAME, request);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();
        
        assertNotEquals(USERNAME, savedUser.getUsername()); 
        assertEquals(newUsername, savedUser.getUsername());  
    }

    @Test
    public void changeUsername_shouldThrowBadRequest_whenNewUsernameExists() {
        String newUsername = USERNAME; 
        ChangeUsernameRequest request = new ChangeUsernameRequest(newUsername, RAW_PASSWORD);
        
        when(userRepository.findByUsername(newUsername)).thenReturn(Optional.of(new User(newUsername, ENCODED_PASSWORD)));
        
        BadRequestException exception = assertThrows(BadRequestException.class, 
            () -> userService.changeUsername(USERNAME, request)
        );
        
        assertEquals("Username already exists.", exception.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    // Change Password Test
    
    @Test
    public void changePassword_shouldUpdatePassword_whenValid() {
        String currentPassword = RAW_PASSWORD;
        String newPassword = "newPassword";
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        
        when(passwordEncoder.matches(RAW_PASSWORD, currentPassword)).thenReturn(true);
        userService.changePassword(USERNAME, currentPassword, newPassword);

        verify(userRepository).save(userCaptor.capture()); 
        User savedUser = userCaptor.getValue();
        
        assertNotEquals(currentPassword, savedUser.getPassword());
        assertEquals(newPassword, savedUser.getPassword());

    }
}
