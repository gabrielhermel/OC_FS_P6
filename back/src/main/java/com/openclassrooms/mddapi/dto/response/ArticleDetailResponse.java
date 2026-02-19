package com.openclassrooms.mddapi.dto.response;

import com.openclassrooms.mddapi.dto.model.CommentDTO;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for detailed article view with comments.
 */
public record ArticleDetailResponse(
    Long id,
    String title,
    String content,
    Long topicId,
    String topicName,
    Long authorId,
    String authorName,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<CommentDTO> comments
) {

}