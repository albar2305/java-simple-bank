package albaradimassuntoro.simplebank.controller;

import albaradimassuntoro.simplebank.entitiy.Account;
import albaradimassuntoro.simplebank.entitiy.User;
import albaradimassuntoro.simplebank.model.*;
import albaradimassuntoro.simplebank.repository.AccountRepository;
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
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class AccountControllerTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private AccountRepository accountRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserController userController;

  private String token;

  @BeforeEach
  void setUp() {
    accountRepository.deleteAll();
    userRepository.deleteAll();

    User user = new User();
    user.setUsername("test");
    user.setEmail("test@example.com");
    user.setHashedPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
    user.setFullName("Test Test");
    userRepository.save(user);
    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("test");
    request.setPassword("secret");
    WebResponse<JwtResponse> webResponse = userController.login(request);
    token = webResponse.getData().getAccessToken();
  }

  @Test
  void testRegisterAccountSuccess() throws Exception {
    CreateAccountRequest request = new CreateAccountRequest();
    request.setCurrency("IDR");

    mockMvc.perform(
        post("/api/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<AccountResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(0L, response.getData().getBalance());
      assertEquals("IDR", response.getData().getCurrency());
      assertEquals("test", response.getData().getOwner());
    });
  }

  @Test
  void testListSuccess() throws Exception {
    for (int i = 0; i < 100; i++) {
      Account account = new Account();
      account.setId(UUID.randomUUID().toString());
      account.setOwner("test");
      account.setCurrency(UUID.randomUUID().toString());
      account.setBalance(0L);
      account.setCreatedAt(new Timestamp(new Date().getTime()));
      accountRepository.save(account);

    }
    mockMvc.perform(
        get("/api/accounts")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<List<AccountResponse>> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertEquals(10, response.getData().size());
      assertEquals(10, response.getPaging().getTotalPage());
      assertEquals(0, response.getPaging().getCurrentPage());
      assertEquals(10, response.getPaging().getSize());
    });
  }
}