package com.app.bank.dto;

public class PasswordVerificationRequest {
    private String currentPassword;
    private String newValue;

    public PasswordVerificationRequest() {}

    public PasswordVerificationRequest(String currentPassword, String newValue) {
        this.currentPassword = currentPassword;
        this.newValue = newValue;
    }

    public String getCurrentPassword() {
        return currentPassword;
    }

    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    public String getNewValue() {
        return newValue;
    }

    public void setNewValue(String newValue) {
        this.newValue = newValue;
    }
}
