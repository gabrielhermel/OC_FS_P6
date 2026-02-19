package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.ArticleRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for article-related business logic.
 */
@Service
@RequiredArgsConstructor
public class ArticleService {

  private final ArticleRepository articleRepository;
  private final UserRepository userRepository;
  private final TopicRepository topicRepository;

  /**
   * Gets all articles from user's subscribed topics.
   *
   * @param topicIds  list of subscribed topic IDs
   * @param ascending true for oldest first, false for newest first
   * @return list of articles
   */
  public List<Article> getArticlesByTopics(List<Long> topicIds, boolean ascending) {
    if (topicIds.isEmpty()) {
      return List.of();
    }

    return ascending
        ? articleRepository.findByTopicIdInOrderByCreatedAtAsc(topicIds)
        : articleRepository.findByTopicIdInOrderByCreatedAtDesc(topicIds);
  }

  /**
   * Finds an article by ID.
   *
   * @param articleId article ID
   * @return optional article
   */
  public Optional<Article> findById(Long articleId) {
    return articleRepository.findById(articleId);
  }

  /**
   * Gets article by ID (as opposed to findById() which returns Optional).
   *
   * @param articleId article ID
   * @return article
   * @throws ResourceNotFoundException if article not found
   */
  public Article getById(Long articleId) {
    return articleRepository.findById(articleId)
        .orElseThrow(() -> new ResourceNotFoundException("Article non trouvé"));
  }

  /**
   * Creates a new article.
   *
   * @param userId  user ID (author)
   * @param topicId topic ID
   * @param title   article title
   * @param content article content
   * @return created article
   */
  @Transactional
  public Article createArticle(Long userId, Long topicId, String title, String content) {
    User author = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    Topic topic = topicRepository.findById(topicId)
        .orElseThrow(() -> new ResourceNotFoundException("Thème non trouvé"));

    Article article = new Article();
    article.setAuthor(author);
    article.setTopic(topic);
    article.setTitle(title);
    article.setContent(content);

    return articleRepository.save(article);
  }
}