package albaradimassuntoro.simplebank.controller;

import albaradimassuntoro.simplebank.entitiy.Account;
import albaradimassuntoro.simplebank.entitiy.User;
import albaradimassuntoro.simplebank.model.*;
import albaradimassuntoro.simplebank.repository.AccountRepository;
import albaradimassuntoro.simplebank.repository.EntryRepository;
import albaradimassuntoro.simplebank.repository.TransferRepository;
import albaradimassuntoro.simplebank.repository.UserRepository;
import albaradimassuntoro.simplebank.security.BCrypt;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.Timestamp;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@AutoConfigureMockMvc
@SpringBootTest
class TransferControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private EntryRepository entryRepository;

  @Autowired
  private TransferRepository transferRepository;

  private String token;

  @Autowired
  private UserController userController;

  @BeforeEach
  void setUp() {
    entryRepository.deleteAll();
    transferRepository.deleteAll();
    accountRepository.deleteAll();
    userRepository.deleteAll();

    User user1 = new User();
    user1.setUsername("test");
    user1.setEmail("test@example.com");
    user1.setHashedPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
    user1.setFullName("Test Test");
    userRepository.save(user1);
    User user2 = new User();
    user2.setUsername("tast");
    user2.setEmail("tast@example.com");
    user2.setHashedPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
    user2.setFullName("Tast Tast");
    userRepository.save(user2);
    Account account1 = new Account();
    account1.setId("account1");
    account1.setBalance(20000L);
    account1.setOwner(user1.getUsername());
    account1.setCurrency("IDR");
    account1.setCreatedAt(new Timestamp(new Date().getTime()));
    accountRepository.save(account1);
    Account account2 = new Account();
    account2.setId("account2");
    account2.setBalance(10000L);
    account2.setOwner(user2.getUsername());
    account2.setCurrency("IDR");
    account2.setCreatedAt(new Timestamp(new Date().getTime()));
    accountRepository.save(account2);
    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("test");
    request.setPassword("secret");
    WebResponse<JwtResponse> webResponse = userController.login(request);
    token = webResponse.getData().getAccessToken();
  }

  @Test
  void transferSuccess() throws Exception {
    CreateTransferRequest request = new CreateTransferRequest();
    request.setFromAccountId("account1");
    request.setToAccountId("account2");
    request.setCurrency("IDR");
    request.setAmount(10000L);

    mockMvc.perform(
        post("/api/transfers")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<TransferResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(20000L, response.getData().getToAccount().getBalance());
      assertEquals(10000L, response.getData().getFromAccount().getBalance());
    });
  }
}