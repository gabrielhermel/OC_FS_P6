package com.openclassrooms.mddapi.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Domain-facing interface for user authentication in MDD. Extends Spring Security's
 * UserDetailsService so Spring can consume it, while adding app-specific authentication needs.
 */
public interface MddUserDetailsService extends UserDetailsService {

  /**
   * Loads user by ID for JWT token validation.
   *
   * @param userId user ID
   * @return UserDetails for Spring Security
   * @throws UsernameNotFoundException if user not found
   */
  UserDetails loadUserById(Long userId) throws UsernameNotFoundException;
}