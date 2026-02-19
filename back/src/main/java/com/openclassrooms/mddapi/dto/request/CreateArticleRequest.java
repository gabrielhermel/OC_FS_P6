package com.openclassrooms.mddapi.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * DTO for creating a new article.
 */
public record CreateArticleRequest(
    @NotNull(message = "Le thème est requis")
    Long topicId,

    @NotBlank(message = "Le titre est requis")
    String title,

    @NotBlank(message = "Le contenu est requis")
    String content
) {

}