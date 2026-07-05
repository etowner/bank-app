package com.app.bank.service;

import com.app.bank.dto.request.ChangeUsernameRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ManagementService {
    private final UserService userService;
    private final AccountService accountService;
    
    public ManagementService(UserService userService, AccountService accountService) {
        this.userService = userService;
        this.accountService = accountService;
    }

    @Transactional
    public void changeUsername(String currentUsername, ChangeUsernameRequest request) {
        userService.changeUsername(currentUsername, request);
        accountService.updateAccountUsernames(currentUsername, request.getNewUsername());
    }

    @Transactional
    public void deleteUser(String username) {
        accountService.deleteUserAccounts(username);
        userService.deleteUser(username);
    }
}