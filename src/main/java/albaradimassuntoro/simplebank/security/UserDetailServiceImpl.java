package albaradimassuntoro.simplebank.security;

import albaradimassuntoro.simplebank.entitiy.User;
import albaradimassuntoro.simplebank.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UserDetailServiceImpl implements UserDetailsService {

  @Autowired
  private UserRepository userRepository;
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username);
    log.debug("Entering in loadUserByUsername Method...");
    if(user == null){
      log.error("Username not found: " + username);
      throw new UsernameNotFoundException("could not found user..!!");
    }
    log.info("User Authenticated Successfully..!!!");
    return new CustomUserDetail(user);
  }
}
