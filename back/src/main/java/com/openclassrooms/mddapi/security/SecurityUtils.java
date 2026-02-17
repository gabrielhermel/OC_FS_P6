package com.openclassrooms.mddapi.security;

import io.jsonwebtoken.JwtException;
import org.springframework.security.core.Authentication;

/**
 * Utility class for security-related operations.
 */
public final class SecurityUtils {

  private SecurityUtils() {
  }

  /**
   * Extracts the user ID from the authentication object. The user ID is stored as the principal
   * name in the JWT subject.
   *
   * @param authentication the authentication object
   * @return the user ID
   * @throws JwtException if the authentication name is not a valid user ID
   */
  public static Long getUserId(Authentication authentication) {
    try {
      return Long.parseLong(authentication.getName());
    } catch (NumberFormatException e) {
      throw new JwtException("Invalid token subject");
    }
  }
}