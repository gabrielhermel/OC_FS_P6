package com.openclassrooms.mddapi.dto.request;

import jakarta.validation.constraints.NotBlank;

/**
 * DTO for creating a new comment.
 */
public record CreateCommentRequest(
    @NotBlank(message = "Le contenu du commentaire est requis")
    String content
) {

}