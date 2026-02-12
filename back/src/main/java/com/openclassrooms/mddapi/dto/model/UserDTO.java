package com.openclassrooms.mddapi.dto.model;

import java.time.LocalDateTime;

/**
 * DTO for User information in API responses. Does not include password for security.
 */
public record UserDTO(
    Long id,
    String username,
    String email,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}