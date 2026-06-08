package com.app.bank;

import com.app.bank.dto.request.TransferRequest;
import com.app.bank.model.Account;
import com.app.bank.model.User;
import com.app.bank.service.AccountService;
import com.app.bank.repo.AccountRepository;
import com.app.bank.repo.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class TransferTransactionTest {

    @Autowired
    private AccountService accountService;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Account account1;
    private Account account2;

    @BeforeEach
    void setUp() {
        User user = new User("testuser", passwordEncoder.encode("password"));
        userRepository.insert(user);

        account1 = new Account("testuser", "4000", "CHECKING");
        account1.setBalance(1000.0);
        accountRepository.insert(account1);

        account2 = new Account("testuser", "5000", "SAVINGS");
        account2.setBalance(500.0);
        accountRepository.insert(account2);
    }

    @AfterEach
    void tearDown() {
        accountRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    void transfer_shouldRollback_whenExceptionThrown() {
        assertThrows(RuntimeException.class, () -> {
            accountService.transfer("testuser", new TransferRequest("4000", "5000", 200.0));
        });

        Account updatedAccount1 = accountRepository.findByAccountNumber("4000").orElseThrow();
        Account updatedAccount2 = accountRepository.findByAccountNumber("5000").orElseThrow();

        assertThat(updatedAccount1.getBalance()).isEqualTo(1000.0);
        assertThat(updatedAccount2.getBalance()).isEqualTo(500.0);
    }
}