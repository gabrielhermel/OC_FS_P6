package com.openclassrooms.mddapi.dto.model;

import java.time.LocalDateTime;

/**
 * DTO for Comment information.
 */
public record CommentDTO(
    Long id,
    String content,
    Long authorId,
    String authorName,
    LocalDateTime createdAt
) {

}