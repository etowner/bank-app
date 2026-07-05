package com.app.bank.dto.request;

import jakarta.validation.constraints.NotBlank;

public class ChangeUsernameRequest {
    
    @NotBlank(message = "Current password is required")
    private String currentPassword;

    @NotBlank(message = "New username is required")
    private String newUsername;

    public ChangeUsernameRequest() {}

    public ChangeUsernameRequest(String currentPassword, String newUsername) {
        this.currentPassword = currentPassword;
        this.newUsername = newUsername;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewUsername() {
        return newUsername;
    }

    public void setNewUsername(String newUsername) {
        this.newUsername = newUsername;
    }

}
