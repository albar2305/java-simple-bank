package albaradimassuntoro.simplebank.controller;

import albaradimassuntoro.simplebank.entitiy.User;
import albaradimassuntoro.simplebank.model.*;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.MockMvcBuilder.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerTest {

  @Autowired
  private MockMvc mockMvc;


  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private UserController userController;


  @BeforeEach
  void setUp() {
    userRepository.deleteAll();
  }

  @Test
  void testRegisterSuccess() throws Exception {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest();
    registerUserRequest.setUsername("test");
    registerUserRequest.setPassword("secret");
    registerUserRequest.setEmail("test@example.com");
    registerUserRequest.setFullName("Test Test");

    mockMvc.perform(
        post("/api/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerUserRequest))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertEquals("OK", response.getData());
    });
  }

  @Test
  void testRegisterBadRequest() throws Exception {
    RegisterUserRequest registerUserRequest = new RegisterUserRequest();
    registerUserRequest.setUsername("");
    registerUserRequest.setPassword("");
    registerUserRequest.setEmail("");
    registerUserRequest.setFullName("");

    mockMvc.perform(
        post("/api/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerUserRequest))
    ).andExpectAll(
        status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void testRegisterDuplicate() throws Exception {
    User user = new User();
    user.setUsername("test");
    user.setEmail("test@example.com");
    user.setHashedPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
    user.setFullName("Test Test");
    userRepository.save(user);

    RegisterUserRequest registerUserRequest = new RegisterUserRequest();
    registerUserRequest.setUsername("test");
    registerUserRequest.setPassword("secret");
    registerUserRequest.setEmail("test@example.com");
    registerUserRequest.setFullName("Test Test");

    mockMvc.perform(
        post("/api/register")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(registerUserRequest))
    ).andExpectAll(
        status().isBadRequest()
    ).andDo(result -> {
      WebResponse<String> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNotNull(response.getErrors());
    });
  }

  @Test
  void loginSuccess() throws Exception {
    User user = new User();
    user.setUsername("test");
    user.setEmail("test@example.com");
    user.setHashedPassword(BCrypt.hashpw("secret", BCrypt.gensalt()));
    user.setFullName("Test Test");
    userRepository.save(user);

    LoginUserRequest request = new LoginUserRequest();
    request.setUsername("test");
    request.setPassword("secret");

    mockMvc.perform(
        post("/api/login")
            .accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(request))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<JwtResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });
      assertNull(response.getErrors());
      assertNotNull(response.getData().getAccessToken());
    });
  }

  @Test
  void updateUserSuccess() throws Exception {
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
    String token = webResponse.getData().getAccessToken();

    UpdateUserRequest userRequest = new UpdateUserRequest();
    userRequest.setPassword("rahasia");
    userRequest.setFullName("Bala Bala");

    mockMvc.perform(
        patch("/api/users")
            .accept(MediaType.APPLICATION_JSON)
            .header(HttpHeaders.AUTHORIZATION,"Bearer "+ token)
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(userRequest))
    ).andExpectAll(
        status().isOk()
    ).andDo(result -> {
      WebResponse<UserResponse> response = objectMapper.readValue(result.getResponse().getContentAsString(), new TypeReference<>() {
      });

      assertNull(response.getErrors());
      assertEquals("Bala Bala", response.getData().getFullName());
      assertEquals("test", response.getData().getUsername());

      User userDb = userRepository.findById("test").orElse(null);
      assertNotNull(userDb);
      assertTrue(BCrypt.checkpw("rahasia", userDb.getHashedPassword()));
    });
  }
}