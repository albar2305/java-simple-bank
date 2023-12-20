package albaradimassuntoro.simplebank.controller;

import albaradimassuntoro.simplebank.model.*;
import albaradimassuntoro.simplebank.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {

  private final UserService userService;

  @Autowired
  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PostMapping(
      path = "/api/register",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<String> register(@RequestBody RegisterUserRequest request) {
    userService.register(request);
    return WebResponse.<String>builder().data("OK").build();
  }

  @PostMapping("/api/login")
  public WebResponse<JwtResponse> login(@RequestBody LoginUserRequest request){
    JwtResponse response = userService.login(request);
    return WebResponse.<JwtResponse>builder().data(response).build();
  }

  @GetMapping(
      path = "/api/users",produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<UserResponse> get(){
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    UserResponse userResponse = userService.get(username);
    return WebResponse.<UserResponse>builder().data(userResponse).build();
  }

  @PatchMapping(
      path = "/api/users",
      consumes = MediaType.APPLICATION_JSON_VALUE,
      produces = MediaType.APPLICATION_JSON_VALUE
  )
  public WebResponse<UserResponse> update( @RequestBody UpdateUserRequest request) {
    String username = SecurityContextHolder.getContext().getAuthentication().getName();
    UserResponse userResponse = userService.update(username, request);
    return WebResponse.<UserResponse>builder().data(userResponse).build();
  }


}
