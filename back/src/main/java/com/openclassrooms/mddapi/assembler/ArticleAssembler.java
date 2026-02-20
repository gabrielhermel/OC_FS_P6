package com.openclassrooms.mddapi.assembler;

import com.openclassrooms.mddapi.dto.model.ArticleDTO;
import com.openclassrooms.mddapi.dto.model.CommentDTO;
import com.openclassrooms.mddapi.dto.response.ArticleDetailResponse;
import com.openclassrooms.mddapi.mapper.ArticleMapper;
import com.openclassrooms.mddapi.mapper.CommentMapper;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.model.Comment;
import com.openclassrooms.mddapi.repository.CommentRepository;
import com.openclassrooms.mddapi.service.ArticleService;
import com.openclassrooms.mddapi.service.SubscriptionService;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Assembler for Article-related DTOs. Combines data from multiple services to build
 * presentation-ready DTOs.
 */
@Component
@RequiredArgsConstructor
public class ArticleAssembler {

  private final ArticleService articleService;
  private final SubscriptionService subscriptionService;
  private final ArticleMapper articleMapper;
  private final CommentMapper commentMapper;
  private final CommentRepository commentRepository;

  /**
   * Assembles article feed for a user from their subscribed topics.
   *
   * @param userId    user ID
   * @param ascending true for oldest first, false for newest first
   * @return list of articles from subscribed topics
   */
  @Transactional(readOnly = true)
  public List<ArticleDTO> assembleArticleFeed(Long userId, boolean ascending) {
    Set<Long> topicIds = subscriptionService.getSubscribedTopicIds(userId);
    List<Article> articles = articleService.getArticlesByTopics(new ArrayList<>(topicIds),
        ascending);
    return articleMapper.toDTOList(articles);
  }

  /**
   * Assembles article detail response with comments.
   *
   * @param article the article entity
   * @return article detail response with comments
   */
  @Transactional(readOnly = true)
  public ArticleDetailResponse assembleArticleDetail(Article article) {
    List<Comment> comments = commentRepository.findByArticleIdOrderByCreatedAtAsc(article.getId());
    List<CommentDTO> commentDTOs = commentMapper.toDTOList(comments);

    return new ArticleDetailResponse(
        article.getId(),
        article.getTitle(),
        article.getContent(),
        article.getTopic().getId(),
        article.getTopic().getName(),
        article.getAuthor().getId(),
        article.getAuthor().getUsername(),
        article.getCreatedAt(),
        article.getUpdatedAt(),
        commentDTOs
    );
  }
}