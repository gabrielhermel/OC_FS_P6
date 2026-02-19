package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for comment-related business logic.
 */
@Service
@RequiredArgsConstructor
public class CommentService {

  private final CommentRepository commentRepository;
  private final UserRepository userRepository;
  private final ArticleRepository articleRepository;

  /**
   * Creates a new comment on an article.
   *
   * @param userId    user ID (author)
   * @param articleId article ID
   * @param content   comment content
   * @return created comment
   * @throws ResourceNotFoundException if user or article not found
   */
  @Transactional
  public Comment createComment(Long userId, Long articleId, String content) {
    User author = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    Article article = articleRepository.findById(articleId)
        .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé"));

    Comment comment = new Comment();
    comment.setAuthor(author);
    comment.setArticle(article);
    comment.setContent(content);

    return commentRepository.save(comment);
  }
}