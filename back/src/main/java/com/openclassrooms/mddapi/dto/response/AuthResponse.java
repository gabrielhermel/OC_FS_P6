package com.openclassrooms.mddapi.dto.response;

import com.openclassrooms.mddapi.dto.model.UserDTO;

/**
 * DTO for authentication responses containing JWT token.
 */
public record AuthResponse(
    String token,
    String type,
    UserDTO user
) {

  /**
   * Convenience constructor that defaults token type to "Bearer".
   *
   * @param token JWT access token
   * @param user  authenticated user information
   */
  public AuthResponse(String token, UserDTO user) {
    this(token, "Bearer", user);
  }
}