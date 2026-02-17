package com.openclassrooms.mddapi.security;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.ArrayList;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * JPA-backed implementation of MddUserDetailsService. Loads user details from the database for
 * Spring Security authentication.
 */
@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements MddUserDetailsService {

  private final UserRepository userRepository;

  /**
   * Loads user by username or email for login authentication. Tries username first, then falls back
   * to email.
   *
   * @param usernameOrEmail username or email
   * @return UserDetails for Spring Security
   * @throws UsernameNotFoundException if user not found
   */
  @Override
  public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(usernameOrEmail)
        .or(() -> userRepository.findByEmail(usernameOrEmail))
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + usernameOrEmail));

    return new org.springframework.security.core.userdetails.User(
        user.getUsername(),
        user.getPassword(),
        new ArrayList<>()
    );
  }

  /**
   * Loads user by ID for JWT token validation.
   *
   * @param userId user ID
   * @return UserDetails for Spring Security
   * @throws UsernameNotFoundException if user not found
   */
  public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new UsernameNotFoundException("User not found: " + userId));

    return new org.springframework.security.core.userdetails.User(
        user.getId().toString(),
        user.getPassword(),
        new ArrayList<>()
    );
  }
}