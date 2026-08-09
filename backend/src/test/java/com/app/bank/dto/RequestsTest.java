package com.app.bank.dto;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Set;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.app.bank.dto.request.RegisterRequest;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;

@SpringBootTest
public class RequestsTest {

    @Test
    void username_validatesWhenBlankOrNull() {
        RegisterRequest request = new RegisterRequest("", "password");
        
        // Set<ConstraintViolation<RegisterRequest>> violations = Validator.validate(request);
        
        // assertEquals(1, violations.size());
        // assertEquals("must not be blank", violations.iterator().next().getMessage());
    }
    
}
