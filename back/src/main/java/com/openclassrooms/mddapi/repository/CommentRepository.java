package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Comment;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Comment entity database operations.
 */
@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

  /**
   * Finds all comments for a specific article, ordered by creation date ascending.
   *
   * @param articleId article ID
   * @return list of comments ordered oldest first
   */
  List<Comment> findByArticleIdOrderByCreatedAtAsc(Long articleId);
}