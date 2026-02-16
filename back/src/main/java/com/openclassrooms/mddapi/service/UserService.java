package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.exception.DuplicateResourceException;
import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
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
   * Finds user by ID.
   *
   * @param id user ID
   * @return optional user
   */
  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
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

  /**
   * Updates user profile. Only updates fields that are provided (non-null and different from
   * current).
   *
   * @param userId   user ID
   * @param username new username (optional)
   * @param email    new email (optional)
   * @param password new password (optional, will be encoded)
   * @return updated user and boolean indicating if changes were made
   */
  @Transactional
  public UpdateResult updateProfile(Long userId, String username, String email, String password) {
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));

    boolean hasChanges = false;

    if (username != null && !username.isBlank() && !username.equals(user.getUsername())) {
      if (userRepository.existsByUsername(username)) {
        throw new DuplicateResourceException("Ce nom d'utilisateur est déjà pris");
      }
      user.setUsername(username);
      hasChanges = true;
    }

    if (email != null && !email.isBlank() && !email.equals(user.getEmail())) {
      if (userRepository.existsByEmail(email)) {
        throw new DuplicateResourceException("Cet email est déjà pris");
      }
      user.setEmail(email);
      hasChanges = true;
    }

    if (password != null && !password.isBlank()) {
      user.setPassword(passwordEncoder.encode(password));
      hasChanges = true;
    }

    if (hasChanges) {
      user = userRepository.save(user);
    }

    return new UpdateResult(user, hasChanges);
  }

  /**
   * Result of an update operation.
   */
  public record UpdateResult(User user, boolean hasChanges) {

  }
}