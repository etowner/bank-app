// package com.app.bank;

// import static org.mockito.Mockito.doThrow;
// import static org.mockito.Mockito.when;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
// import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
// import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// import java.util.List;
// import java.util.Optional;

// import com.app.bank.api.AccountController;
// import com.app.bank.exception.BadRequestException;
// import com.app.bank.exception.ResourceNotFoundException;
// import com.app.bank.model.Account;
// import com.app.bank.service.AccountService;
// import com.app.bank.service.UserService;
// import org.junit.jupiter.api.BeforeEach;
// import org.junit.jupiter.api.Test;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
// import org.springframework.http.MediaType;
// import org.springframework.test.web.servlet.MockMvc;
// import org.springframework.test.context.bean.override.mockito.MockitoBean;
// @WebMvcTest(AccountController.class)
// public class AccountControllerTests {

//     @Autowired
//     private MockMvc mvc;


//     @MockitoBean
//     private AccountService accountService;

//     @MockitoBean
//     private UserService UserService;

//     private Account validAccount;

//     @BeforeEach
//     public void setUp() {
//         validAccount = new Account("testUser", 0, "Checking");
//     }

//     @Test
//     public void getUserAccounts_returnsList() throws Exception {
//         when(accountService.getUserAccounts("testUser")).thenReturn(List.of(validAccount));

//         mvc.perform(get("/api/v1/account/my-accounts").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk())
//                 .andExpect(jsonPath("$[0].userID").value("testUser"))
//                 .andExpect(jsonPath("$[0].type").value("Checking"));
//     }

//     @Test
//     public void getUserAccount_returnsNotFound_whenMissing() throws Exception {
//         when(accountService.getAccount(1234)).thenReturn(Optional.empty());
//         mvc.perform(get("/api/v1/account/1234").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     public void openAccount_returnsOk_whenValid() throws Exception {
//         mvc.perform(post("/api/v1/account/Savings").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     public void deposit_returnsBadRequest_whenInvalidAmount() throws Exception {
//         doThrow(new BadRequestException("Invalid amount.")).when(accountService).depositAmount(1234, 0f);
//         mvc.perform(put("/api/v1/user/testUser/1234/deposit")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("0"))
//                 .andExpect(status().isBadRequest());
//     }

//     @Test
//     public void withdraw_returnsNotFound_whenMissingAccount() throws Exception {
//         doThrow(new ResourceNotFoundException("Account not found.")).when(accountService).withdrawAmount(1234, 100f);
//         mvc.perform(put("/api/v1/user/testUser/1234/withdraw")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("100"))
//                 .andExpect(status().isNotFound());
//     }

//     @Test
//     public void transfer_returnsBadRequest_whenInvalidAmount() throws Exception {
//         mvc.perform(put("/api/v1/user/testUser/1234/5678")
//                 .contentType(MediaType.APPLICATION_JSON)
//                 .content("-25"))
//                 .andExpect(status().isBadRequest());
//     }

//     @Test
//     public void closeAccount_returnsOk_whenExists() throws Exception {
//         mvc.perform(delete("/api/v1/user/testUser/1234/close").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk());
//     }

//     @Test
//     public void closeAll_returnsOk() throws Exception {
//         mvc.perform(delete("/api/v1/user/testUser/closeAll").contentType(MediaType.APPLICATION_JSON))
//                 .andExpect(status().isOk());
//     }
// }
