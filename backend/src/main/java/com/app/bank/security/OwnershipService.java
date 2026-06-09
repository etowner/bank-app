package com.app.bank.security;

import org.springframework.stereotype.Service;

import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.repo.AccountRepository;

import org.springframework.security.access.AccessDeniedException;

@Service
public class OwnershipService {
    
    private final AccountRepository accountRepository;

    public OwnershipService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public void verifyAccountOwnership(String accountNumber, String username) {
        Account account = accountRepository.findByAccountNumber(accountNumber)
            .orElseThrow(() -> new ResourceNotFoundException("Account not found."));
        
        if (!account.getUsername().equals(username)) {
            throw new AccessDeniedException("You do not own this account.");
        }
    }
}
