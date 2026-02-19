package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Article;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Article entity database operations.
 */
@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {

  /**
   * Finds all articles from specified topics, ordered by creation date descending.
   *
   * @param topicIds list of topic IDs
   * @return list of articles ordered newest first
   */
  List<Article> findByTopicIdInOrderByCreatedAtDesc(List<Long> topicIds);

  /**
   * Finds all articles from specified topics, ordered by creation date ascending.
   *
   * @param topicIds list of topic IDs
   * @return list of articles ordered oldest first
   */
  List<Article> findByTopicIdInOrderByCreatedAtAsc(List<Long> topicIds);
}