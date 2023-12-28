package albaradimassuntoro.simplebank.service;

import albaradimassuntoro.simplebank.entitiy.User;
import albaradimassuntoro.simplebank.model.*;
import albaradimassuntoro.simplebank.repository.UserRepository;
import albaradimassuntoro.simplebank.security.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final ValidationService validationService;
  private final JwtService jwtService;


  @Autowired
  public UserServiceImpl(UserRepository userRepository, ValidationService validationService, JwtService jwtService) {
    this.userRepository = userRepository;
    this.validationService = validationService;
    this.jwtService = jwtService;
  }

  @Override
  @Transactional
  public void register(RegisterUserRequest request) {
    validationService.validate(request);

    if (userRepository.existsById(request.getUsername())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username already registered");
    }

    if (userRepository.existsByEmail(request.getEmail())) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
    }
    User user = new User();
    user.setUsername(request.getUsername());
    user.setFullName(request.getFullName());
    user.setEmail(request.getEmail());
    user.setHashedPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
    user.setCreatedAt(new Timestamp(new Date().getTime()));

    userRepository.save(user);
  }

  @Transactional(readOnly = true)
  @Override
  public UserResponse get(String username) {
    User user = userRepository.findByUsername(username);
    return UserResponse.builder().username(user.getUsername()).email(user.getEmail()).createdAt(user.getCreatedAt()).fullName(user.getFullName()).passwordChangedAt(user.getPasswordChangedAt()).build();
  }

  @Transactional
  @Override
  public UserResponse update(String username, UpdateUserRequest request) {
    validationService.validate(request);
    User user = userRepository.findByUsername(username);
    if (Objects.nonNull(request.getFullName())) {
      user.setFullName(request.getFullName());
    }

    if (Objects.nonNull(request.getPassword())) {
      user.setHashedPassword(BCrypt.hashpw(request.getPassword(), BCrypt.gensalt()));
    }

    userRepository.save(user);

    return UserResponse.builder().fullName(user.getFullName()).passwordChangedAt(user.getPasswordChangedAt()).createdAt(user.getCreatedAt()).email(user.getEmail()).username(username).build();
  }

  @Override
  public JwtResponse login(LoginUserRequest request) {
    validationService.validate(request);

    User user = userRepository.findById(request.getUsername())
        .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong"));

    if (BCrypt.checkpw(request.getPassword(), user.getHashedPassword())) {
      return JwtResponse.builder().accessToken(jwtService.generateToken(user.getUsername())).build();
    } else {
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Username or password wrong");
    }
  }
}
