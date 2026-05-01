package com.app.bank.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.User;
import com.app.bank.repo.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

    private User validUser;

    @BeforeEach
    public void setUp() {
        validUser = new User("testUser", "testPass");
    }

    @Test
    public void newUser_shouldThrowBadRequest_whenUserIdIsNull() {
        User user = new User(null, "password");

        assertThrows(BadRequestException.class, () -> userService.newUser(user));
    }

    @Test
    public void newUser_shouldThrowBadRequest_whenPasswordIsBlank() {
        User user = new User("testUser", "");
        assertThrows(BadRequestException.class, () -> userService.newUser(user));
    }

    @Test
    public void newUser_shouldThrowBadRequest_whenUserAlreadyExists() {
        when(userRepository.getUserByUserID(anyString())).thenReturn(Optional.of(validUser));
        assertThrows(BadRequestException.class, () -> userService.newUser(validUser));
    }

    @Test
    public void newUser_shouldInsertUser_whenValid() {
        when(userRepository.getUserByUserID(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("testPass")).thenReturn("encodedPass");

        userService.newUser(validUser);

        verify(userRepository).insert(any(User.class));
    }

    @Test
    public void getUser_shouldReturnOptional_whenPresent() {
        when(userRepository.getUserByUserID("testUser")).thenReturn(Optional.of(validUser));

        Optional<User> result = userService.getUser("testUser");

        assertTrue(result.isPresent());
    }

    @Test
    public void deleteUser_shouldThrowNotFound_whenMissing() {
        when(userRepository.getUserByUserID(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> userService.deleteUser("missingUser"));
    }

    @Test
    public void checkforUserPassword_shouldReturnFalse_whenUserDoesNotExist() {
        when(userRepository.getUserByUserID(anyString())).thenReturn(Optional.empty());

        User request = new User("missingUser", "password");
        assertFalse(userService.checkforUserPassword(request));
    }

    @Test
    public void checkforUserPassword_shouldReturnTrue_whenPasswordMatches() {
        when(userRepository.getUserByUserID(validUser.getUserID())).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("testPass", "testPass")).thenReturn(true);

        assertTrue(userService.checkforUserPassword(validUser));
    }
}
