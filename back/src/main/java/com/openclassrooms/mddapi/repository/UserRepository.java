package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for User entity database operations.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  /**
   * Finds user by email.
   *
   * @param email user email
   * @return optional user
   */
  Optional<User> findByEmail(String email);

  /**
   * Finds user by username.
   *
   * @param username username
   * @return optional user
   */
  Optional<User> findByUsername(String username);

  /**
   * Checks if email already exists.
   *
   * @param email email to check
   * @return true if exists
   */
  Boolean existsByEmail(String email);

  /**
   * Checks if username already exists.
   *
   * @param username username to check
   * @return true if exists
   */
  Boolean existsByUsername(String username);
}