package com.openclassrooms.mddapi.dto.model;

import java.time.LocalDateTime;

/**
 * DTO for Article in list views (feed).
 */
public record ArticleDTO(
    Long id,
    String title,
    String content,
    Long topicId,
    String topicName,
    Long authorId,
    String authorName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {

}