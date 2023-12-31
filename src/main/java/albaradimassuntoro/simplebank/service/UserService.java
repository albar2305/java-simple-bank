package albaradimassuntoro.simplebank.service;

import albaradimassuntoro.simplebank.entitiy.User;
import albaradimassuntoro.simplebank.model.*;

public interface UserService {
  void register(RegisterUserRequest request);
  UserResponse get(String username);

  UserResponse update(String username, UpdateUserRequest request);

  JwtResponse login(LoginUserRequest request);
}
