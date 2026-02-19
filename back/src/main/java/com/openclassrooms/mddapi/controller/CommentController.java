package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.model.CommentDTO;
import com.openclassrooms.mddapi.dto.request.CreateCommentRequest;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.security.SecurityUtils;
import com.openclassrooms.mddapi.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for comment endpoints.
 */
@RestController
@RequestMapping("/api/articles/{articleId}/comments")
@RequiredArgsConstructor
public class CommentController {

  private final CommentService commentService;
  private final CommentMapper commentMapper;

  /**
   * Creates a new comment on an article.
   *
   * @param articleId      article ID
   * @param request        comment data
   * @param authentication current user authentication
   * @return created comment
   */
  @PostMapping
  public ResponseEntity<CommentDTO> createComment(
      @PathVariable Long articleId,
      @Valid @RequestBody CreateCommentRequest request,
      Authentication authentication
  ) {
    Long userId = SecurityUtils.getUserId(authentication);

    Comment comment = commentService.createComment(
        userId,
        articleId,
        request.content()
    );

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(commentMapper.toDTO(comment));
  }
}