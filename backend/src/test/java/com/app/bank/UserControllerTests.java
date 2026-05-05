// package com.app.bank;

// import static org.mockito.ArgumentMatchers.any;
// import static org.mockito.Mockito.doThrow;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


// import java.util.Optional;

// import com.app.bank.api.UserController;
// import com.app.bank.exception.ResourceNotFoundException;
// import com.app.bank.model.User;
// import com.app.bank.service.AccountService;
// import com.app.bank.service.UserService;
// import tools.jackson.databind.ObjectMapper;

// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.BadCredentialsException;
// import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
// import org.springframework.test.web.servlet.MockMvc;

// @WebMvcTest(UserController.class)
// public class UserControllerTests {

//     @Autowired
//     private MockMvc mvc;

//     @MockitoBean
//     private UserService userService;
    
//     @MockitoBean
//     private AccountService accountService;

//     @MockitoBean
//     private AuthenticationManager authenticationManager;

//     private final ObjectMapper objectMapper = new ObjectMapper();

//     @Test
//     public void getUser_returnsUser_whenFound() throws Exception {
//         User user = new User("testUser", "testPass");
//         when(userService.getUser("testUser")).thenReturn(Optional.of(user));

//         mvc.perform(get("/api/v1/user/testUser").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//             .andExpect(jsonPath("$.userID").value("testUser"))
//             .andExpect(jsonPath("$.password").doesNotExist());
//     }


//     @Test
//     public void register_returnsBadRequest_whenPayloadMissingFields() throws Exception {
//         User user = new User("", "password");
//         mvc.perform(post("/api/v1/user/register")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isBadRequest());
//     }

//     @Test
//     public void login_returnsBadRequest_whenPayloadMissingFields() throws Exception {
//         User user = new User("testUser", "");
//         mvc.perform(post("/api/v1/user/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isBadRequest());
//     }

//    @Test
//     public void login_returnsUnauthorized_whenUserNotFound() throws Exception {
//         User user = new User("testUser", "wrongPass");
//         when(userService.validateUser(any(User.class))).thenReturn(true);
//         when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//             .thenThrow(new BadCredentialsException("Bad credentials"));

//         mvc.perform(post("/api/v1/user/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isUnauthorized());
//     }

//     @Test
//     public void login_returnsUnauthorized_whenPasswordIncorrect() throws Exception {
//         User user = new User("testUser", "wrongPass");
//         when(userService.validateUser(any(User.class))).thenReturn(true);
//         when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//             .thenThrow(new BadCredentialsException("Bad credentials"));

//         mvc.perform(post("/api/v1/user/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isUnauthorized());
//     }


//     @Test
//     public void login_returnsOk_whenCredentialsValid() throws Exception {
//         User user = new User("testUser", "testPass");
//         when(userService.validateUser(any(User.class))).thenReturn(true);
//         when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
//             .thenReturn(new UsernamePasswordAuthenticationToken("testUser", null));

//         mvc.perform(post("/api/v1/user/login")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content(objectMapper.writeValueAsString(user)))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     public void deleteUser_returnsNotFound_whenUserMissing() throws Exception {
//         doThrow(new ResourceNotFoundException("User not found.")).when(userService).deleteUser("missingUser");

//         mvc.perform(delete("/api/v1/user/missingUser").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     public void deleteUser_returnsOk_whenUserExists() throws Exception {
//         mvc.perform(delete("/api/v1/user/testUser").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk());
//     }
// }
