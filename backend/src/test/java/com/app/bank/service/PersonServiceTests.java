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
import com.app.bank.model.Person;
import com.app.bank.repo.PersonRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class PersonServiceTests {

    @Mock
    private PersonRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private PersonService personService;

    private Person validUser;

    @BeforeEach
    public void setUp() {
        validUser = new Person("testUser", "testPass");
    }

    @Test
    public void newUser_shouldThrowBadRequest_whenUserIdIsNull() {
        Person user = new Person(null, "password");

        assertThrows(BadRequestException.class, () -> personService.newUser(user));
    }

    @Test
    public void newUser_shouldThrowBadRequest_whenPasswordIsBlank() {
        Person user = new Person("testUser", "");
        assertThrows(BadRequestException.class, () -> personService.newUser(user));
    }

    @Test
    public void newUser_shouldThrowBadRequest_whenUserAlreadyExists() {
        when(userRepository.getUserByUserID(anyString())).thenReturn(Optional.of(validUser));
        assertThrows(BadRequestException.class, () -> personService.newUser(validUser));
    }

    @Test
    public void newUser_shouldInsertUser_whenValid() {
        when(userRepository.getUserByUserID(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("testPass")).thenReturn("encodedPass");

        personService.newUser(validUser);

        verify(userRepository).insert(any(Person.class));
    }

    @Test
    public void getUser_shouldReturnOptional_whenPresent() {
        when(userRepository.getUserByUserID("testUser")).thenReturn(Optional.of(validUser));

        Optional<Person> result = personService.getUser("testUser");

        assertTrue(result.isPresent());
    }

    @Test
    public void deleteUser_shouldThrowNotFound_whenMissing() {
        when(userRepository.getUserByUserID(anyString())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> personService.deleteUser("missingUser"));
    }

    @Test
    public void checkforUserPassword_shouldReturnFalse_whenUserDoesNotExist() {
        when(userRepository.getUserByUserID(anyString())).thenReturn(Optional.empty());

        Person request = new Person("missingUser", "password");
        assertFalse(personService.checkforUserPassword(request));
    }

    @Test
    public void checkforUserPassword_shouldReturnTrue_whenPasswordMatches() {
        when(userRepository.getUserByUserID(validUser.getUserID())).thenReturn(Optional.of(validUser));
        when(passwordEncoder.matches("testPass", "testPass")).thenReturn(true);

        assertTrue(personService.checkforUserPassword(validUser));
    }
}
