package com.app.bank.api;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import java.util.ArrayList;

import com.app.bank.dto.request.ChangePasswordRequest;
import com.app.bank.dto.request.ChangeUsernameRequest;
import com.app.bank.dto.request.LoginRequest;
import com.app.bank.dto.request.RegisterRequest;
import com.app.bank.dto.response.UserResponse;
import com.app.bank.exception.BadRequestException;
import com.app.bank.exception.ResourceNotFoundException;
import com.app.bank.model.Account;
import com.app.bank.model.User;
import com.app.bank.service.ManagementService;
import com.app.bank.service.UserService;
import tools.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@DisplayName("UserController Integration Tests")
public class UserControllerTests {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private ManagementService managementService;

    @MockitoBean
    private AuthenticationManager authenticationManager;

    @Autowired
    private ObjectMapper objectMapper;

    private String testUsername = "testuser";
    private String testPassword = "password123";
    private String encodedPassword = "encoded_testPass";


    @BeforeEach
    void setUp() {
    }

    // ======================== Register Tests ========================
    @Nested
    @DisplayName("POST /api/v1/user/register")
    class RegisterTests {

        @Test
        @DisplayName("Should register user successfully")
        void shouldRegisterUserSuccessfully() throws Exception {
            RegisterRequest request = new RegisterRequest(testUsername, testPassword);
            doNothing().when(userService).register(any(RegisterRequest.class));
            
            Authentication auth = new UsernamePasswordAuthenticationToken(testUsername, testPassword);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(auth);

            mvc.perform(post("/api/v1/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Account created successfully."));

            verify(userService, times(1)).register(any(RegisterRequest.class));
            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        @DisplayName("Should return 400 when username already exists")
        void shouldReturnBadRequestWhenUsernameExists() throws Exception {
            RegisterRequest request = new RegisterRequest(testUsername, testPassword);
            doThrow(new BadRequestException("Username already exists."))
                    .when(userService).register(any(RegisterRequest.class));

            mvc.perform(post("/api/v1/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Should return 400 when validation fails")
        void shouldReturnBadRequestWhenValidationFails() throws Exception {
            RegisterRequest request = new RegisterRequest("", testPassword);

            mvc.perform(post("/api/v1/user/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ======================== Login Tests ========================
    @Nested
    @DisplayName("POST /api/v1/user/login")
    class LoginTests {

        @Test
        @DisplayName("Should login user successfully")
        void shouldLoginUserSuccessfully() throws Exception {
            LoginRequest request = new LoginRequest(testUsername, testPassword);
            Authentication auth = new UsernamePasswordAuthenticationToken(testUsername, testPassword);
            
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(auth);

            mvc.perform(post("/api/v1/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Logged in"));

            verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        }

        @Test
        @DisplayName("Should return 401 when credentials are invalid")
        void shouldReturnUnauthorizedWhenCredentialsInvalid() throws Exception {
            LoginRequest request = new LoginRequest(testUsername, "wrongpassword");
            
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            mvc.perform(post("/api/v1/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }

        @Test
        @DisplayName("Should return 400 when validation fails")
        void shouldReturnBadRequestWhenValidationFails() throws Exception {
            LoginRequest request = new LoginRequest("", testPassword);

            mvc.perform(post("/api/v1/user/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ======================== Get Current User Tests ========================
    @Nested
    @DisplayName("GET /api/v1/user")
    class GetCurrentUserTests {

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should return current user successfully")
        void shouldReturnCurrentUserSuccessfully() throws Exception {
            User user = new User(testUsername, testPassword);
          
            UserResponse response = new UserResponse(user);
            
            when(userService.getUser(testUsername)).thenReturn(response);

            mvc.perform(get("/api/v1/user")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.username").value(testUsername));
                   

            verify(userService, times(1)).getUser(testUsername);
        }

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should return 404 when user not found")
        void shouldReturnNotFound_WhenUserNotExists() throws Exception {
            when(userService.getUser(testUsername))
                    .thenThrow(new ResourceNotFoundException("User not found."));

            mvc.perform(get("/api/v1/user")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    // ======================== Change Password Tests ========================
    @Nested
    @DisplayName("PUT /api/v1/user/change-password")
    class ChangePasswordTests {

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should change password successfully")
        void shouldChangePasswordSuccessfully() throws Exception {
            String currentPassword = "oldpassword";
            String newPassword = "newpassword";
            ChangePasswordRequest request = new ChangePasswordRequest(currentPassword, newPassword);
            
            doNothing().when(userService).changePassword(testUsername, currentPassword, newPassword);

            mvc.perform(put("/api/v1/user/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Password updated successfully"));

            verify(userService, times(1)).changePassword(testUsername, currentPassword, newPassword);
        }

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should return 400 when current password is incorrect")
        void shouldReturnBadRequestWhenCurrentPasswordIncorrect() throws Exception {
            String currentPassword = "wrongpassword";
            String newPassword = "newpassword";
            ChangePasswordRequest request = new ChangePasswordRequest(currentPassword, newPassword);
            
            doThrow(new BadRequestException("Current password is incorrect."))
                    .when(userService).changePassword(testUsername, currentPassword, newPassword);

            mvc.perform(put("/api/v1/user/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should return 400 when validation fails")
        void shouldReturnBadRequestWhenValidationFails() throws Exception {
            ChangePasswordRequest request = new ChangePasswordRequest("", "newpassword");

            mvc.perform(put("/api/v1/user/change-password")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }
    }

    // ======================== Change Username Tests ========================
    @Nested
    @DisplayName("PUT /api/v1/user/change-username")
    class ChangeUsernameTests {

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should change username successfully")
        void shouldChangeUsernameSuccessfully() throws Exception {
            String newUsername = "newusername";
            String password = "password123";
            ChangeUsernameRequest request = new ChangeUsernameRequest(newUsername, password);
            
            doNothing().when(managementService).changeUsername(eq(testUsername), any(ChangeUsernameRequest.class));
            Authentication auth = new UsernamePasswordAuthenticationToken(newUsername, password);
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenReturn(auth);

            mvc.perform(put("/api/v1/user/change-username")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Username updated successfully. Please log in again with your new username."));

            verify(managementService, times(1)).changeUsername(eq(testUsername), any(ChangeUsernameRequest.class));
        }

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should return 400 when username already exists")
        void shouldReturnBadRequestWhenUsernameExists() throws Exception {
            String newUsername = "existinguser";
            String password = "password123";
            ChangeUsernameRequest request = new ChangeUsernameRequest(newUsername, password);
            
            doThrow(new BadRequestException("Username already exists."))
                    .when(managementService).changeUsername(eq(testUsername), any(ChangeUsernameRequest.class));

            mvc.perform(put("/api/v1/user/change-username")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isBadRequest());
        }

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should return 401 when password is incorrect")
        void shouldReturnUnauthorizedWhenPasswordIncorrect() throws Exception {
            String newUsername = "newusername";
            String wrongPassword = "wrongpassword";
            ChangeUsernameRequest request = new ChangeUsernameRequest(newUsername, wrongPassword);
            
            doNothing().when(managementService).changeUsername(eq(testUsername), any(ChangeUsernameRequest.class));
            when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                    .thenThrow(new BadCredentialsException("Invalid credentials"));

            mvc.perform(put("/api/v1/user/change-username")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isUnauthorized());
        }
    }

    // ======================== Delete User Tests ========================
    @Nested
    @DisplayName("DELETE /api/v1/user")
    class DeleteUserTests {

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should delete user successfully")
        void shouldDeleteUserSuccessfully() throws Exception {
            doNothing().when(managementService).deleteUser(testUsername);

            mvc.perform(delete("/api/v1/user")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isOk())
                    .andExpect(content().string("Account deleted successfully"));

            verify(managementService, times(1)).deleteUser(testUsername);
        }

        @Test
        @WithMockUser(username = "testuser")
        @DisplayName("Should return 404 when user not found")
        void shouldReturnNotFoundWhenUserNotExists() throws Exception {
            doThrow(new ResourceNotFoundException("User not found."))
                    .when(managementService).deleteUser(testUsername);

            mvc.perform(delete("/api/v1/user")
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(status().isNotFound());
        }
    }
}
