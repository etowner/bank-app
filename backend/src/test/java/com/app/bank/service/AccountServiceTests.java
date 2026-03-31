package com.app.bank.service;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.repo.AccountRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.MongoTemplate;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTests {

    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountService accountService;

    @Mock
    private PersonService personService;

    @Mock
    private MongoTemplate mongoTemplate;

    private Account validAccount;

    @BeforeEach
    public void setUp() {
        validAccount = new Account("testUser", "Checking"); 
    }

    @Test
    public void newAccount_shouldThrowBadRequest_whenUserIdIsNull() {
        Account account = new Account(null, "Checking");
        assertThrows(BadRequestException.class, () -> accountService.newAccount(account));
    }

    @Test
    public void newAccount_shouldThrowBadRequest_whenTypeIsBlank() {
        Account account = new Account("testUser", "");
        assertThrows(BadRequestException.class, () -> accountService.newAccount(account));
    }

    @Test
    public void newAccount_shouldThrowResourceNotFound_whenUserDoesNotExist() {
        when(personService.checkforUser(anyString())).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> accountService.newAccount(validAccount));
    }


    @Test
    public void isAccount_shouldReturnFalse_whenAccountDoesNotExist() {
        when(accountRepository.getAccountByAccountID(anyInt())).thenReturn(Optional.empty());
        assertFalse(accountService.isAccount(1234));    
    }

    @Test
    public void isAccount_shouldReturnTrue_whenAccountExists() {
        when(accountRepository.getAccountByAccountID(anyInt())).thenReturn(Optional.of(validAccount));
        assertTrue(accountService.isAccount(1234));
    }

    @Test
    public void deposit_shouldThrowResourceNotFound_whenAccountDoesNotExist() {
        when(accountRepository.getAccountByAccountID(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount(1234));
    }

    @Test
    public void deposit_shouldThrowBadRequest_whenAmountIsInvalid() {
        float invalidAmount = -50.0f;
        assertThrows(BadRequestException.class, () -> {
            accountService.depositAmount(1234, invalidAmount);
        });
    }
    
    @Test
    public void withdraw_shouldThrowBadRequest_whenAmountIsInvalid() {
        float invalidAmount = -50.0f;
        assertThrows(BadRequestException.class, () -> {
            accountService.withdrawAmount(1234, invalidAmount);
        });
    }
    @Test
    public void withdraw_shouldThrowResourceNotFound_whenAccountDoesNotExist() {
        when(accountRepository.getAccountByAccountID(anyInt())).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> accountService.deleteAccount(1234));
    }

    @Test
    public void withdraw_shouldThrowBadRequest_whenInsufficientFunds() {
        float amount = 1000.0f;
        when(accountRepository.getAccountByAccountID(anyInt())).thenReturn(Optional.of(validAccount));
        assertThrows(BadRequestException.class, () -> {
            accountService.withdrawAmount(1234, amount);
        });
    }

    @Test
    public void transfer_shouldThrowBadRequest_whenAmountIsInvalid() {
        float invalidAmount = -50.0f;
        assertThrows(BadRequestException.class, () -> {
            accountService.transfer(1234, 5678, invalidAmount);
        });
    }

    @Test
    public void transfer_shouldThrowResourceNotFound_whenSourceAccountDoesNotExist() {
        when(accountRepository.getAccountByAccountID(1234)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.transfer(1234, 5678, 100.0f);
        });
    }

    @Test
    public void transfer_shouldThrowResourceNotFound_whenDestinationAccountDoesNotExist() {
        when(accountRepository.getAccountByAccountID(1234)).thenReturn(Optional.of(validAccount));
        when(accountRepository.getAccountByAccountID(5678)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.transfer(1234, 5678, 100.0f);
        });
    }

    @Test
    public void deleteAccount_shouldSucceed_whenAccountExists() {
        when(accountRepository.getAccountByAccountID(anyInt())).thenReturn(Optional.of(validAccount));
        accountService.deleteAccount(1234); 
    }

}
