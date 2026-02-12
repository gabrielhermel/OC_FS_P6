package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for user-related business logic.
 */
@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  /**
   * Registers a new user with encrypted password.
   *
   * @param user user to register
   * @return registered user
   */
  @Transactional
  public User registerUser(User user) {
    user.setPassword(passwordEncoder.encode(user.getPassword()));
    return userRepository.save(user);
  }

  /**
   * Finds user by email.
   *
   * @param email email
   * @return optional user
   */
  public Optional<User> findByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  /**
   * Finds user by username.
   *
   * @param username username
   * @return optional user
   */
  public Optional<User> findByUsername(String username) {
    return userRepository.findByUsername(username);
  }

  /**
   * Checks if email is already taken.
   *
   * @param email email to check
   * @return true if exists
   */
  public Boolean existsByEmail(String email) {
    return userRepository.existsByEmail(email);
  }

  /**
   * Checks if username is already taken.
   *
   * @param username username to check
   * @return true if exists
   */
  public Boolean existsByUsername(String username) {
    return userRepository.existsByUsername(username);
  }
}