package com.app.bank.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public class TransferRequest {
    
    @NotBlank(message = "Source account number is required")
    private String accountNumber1;

    @NotBlank(message = "Destination account number is required")
    private String accountNumber2;

    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Amount must be greater than zero")
    private BigDecimal amount;

    public TransferRequest() {}

    public TransferRequest(String accountNumber1, String accountNumber2, BigDecimal amount) {
        this.accountNumber1 = accountNumber1;
        this.accountNumber2 = accountNumber2;
        this.amount = amount;
    }

    public String getFromAccountNumber() {
        return accountNumber1;
    }

    public void setFromAccountNumber(String accountNumber1) {
        this.accountNumber1 = accountNumber1;
    }

    public String getToAccountNumber() {
        return accountNumber2;
    }

    public void setToAccountNumber(String accountNumber2) {
        this.accountNumber2 = accountNumber2;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
}
