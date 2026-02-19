package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.assembler.ArticleAssembler;
import com.openclassrooms.mddapi.dto.model.ArticleDTO;
import com.openclassrooms.mddapi.dto.request.CreateArticleRequest;
import com.openclassrooms.mddapi.dto.response.ArticleDetailResponse;
import com.openclassrooms.mddapi.model.Article;
import com.openclassrooms.mddapi.security.SecurityUtils;
import com.openclassrooms.mddapi.service.ArticleService;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for article endpoints.
 */
@RestController
@RequestMapping("/api/articles")
@RequiredArgsConstructor
public class ArticleController {

  private final ArticleService articleService;
  private final ArticleAssembler articleAssembler;

  /**
   * Gets article feed for current user from subscribed topics.
   *
   * @param sort           optional sort order (asc or desc, default: desc)
   * @param authentication current user authentication
   * @return list of articles from subscribed topics
   */
  @GetMapping
  @Transactional(readOnly = true)
  public ResponseEntity<List<ArticleDTO>> getArticleFeed(
      @RequestParam(defaultValue = "desc") String sort,
      Authentication authentication
  ) {
    Long userId = SecurityUtils.getUserId(authentication);
    boolean ascending = "asc".equalsIgnoreCase(sort);

    return ResponseEntity.ok(articleAssembler.assembleArticleFeed(userId, ascending));
  }

  /**
   * Gets a single article with all details and comments.
   *
   * @param articleId article ID
   * @return article details with comments
   */
  @GetMapping("/{articleId}")
  @Transactional(readOnly = true)
  public ResponseEntity<ArticleDetailResponse> getArticle(@PathVariable Long articleId) {
    Article article = articleService.getById(articleId);
    return ResponseEntity.ok(articleAssembler.assembleArticleDetail(article));
  }

  /**
   * Creates a new article.
   *
   * @param request        article data
   * @param authentication current user authentication
   * @return created article details
   */
  @PostMapping
  @Transactional(readOnly = true)
  public ResponseEntity<ArticleDetailResponse> createArticle(
      @Valid @RequestBody CreateArticleRequest request,
      Authentication authentication
  ) {
    Long userId = SecurityUtils.getUserId(authentication);

    Article article = articleService.createArticle(
        userId,
        request.topicId(),
        request.title(),
        request.content()
    );

    return ResponseEntity.status(HttpStatus.CREATED)
        .body(articleAssembler.assembleArticleDetail(article));
  }
}