package com.app.bank;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.app.bank.api.AccountController;
import com.app.bank.api.PersonController;
import com.app.bank.model.Person;
import com.app.bank.service.AccountService;
import com.app.bank.service.DatabaseUserDetailsService;
import com.app.bank.service.PersonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest({ PersonController.class, AccountController.class })
@Import(SecurityConfig.class)
public class AuthenticationSecurityTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private PersonService personService;

    @MockitoBean
    private AccountService accountService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @MockitoBean
    private DatabaseUserDetailsService userDetailsService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void loginEndpoint_allowsUnauthenticatedRequests() throws Exception {
        Person loginPayload = new Person("", "");

        mvc.perform(post("/api/v1/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void registerEndpoint_allowsUnauthenticatedRequests() throws Exception {
        Person registerPayload = new Person("", "");

        mvc.perform(post("/api/v1/user/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(registerPayload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void protectedEndpoint_rejectsUnauthenticatedRequests() throws Exception {
        mvc.perform(get("/api/v1/user/testUser").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isUnauthorized());
    }

    @Test
    public void protectedEndpoint_allowsAuthenticatedRequests() throws Exception {
        when(personService.getUser(any(String.class))).thenReturn(java.util.Optional.of(new Person("testUser", "testPass")));

        mvc.perform(get("/api/v1/user/testUser")
            .with(user("testUser").roles("USER"))
            .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }
}
