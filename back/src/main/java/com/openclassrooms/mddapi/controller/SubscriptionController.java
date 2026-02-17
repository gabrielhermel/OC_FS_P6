package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.security.SecurityUtils;
import com.openclassrooms.mddapi.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for subscription endpoints.
 */
@RestController
@RequestMapping("/api/subscriptions")
@RequiredArgsConstructor
public class SubscriptionController {

  private final SubscriptionService subscriptionService;

  /**
   * Subscribes current user to a topic.
   *
   * @param topicId        topic ID
   * @param authentication current user authentication
   * @return 201 if newly subscribed, 204 if already subscribed
   */
  @PostMapping("/{topicId}")
  public ResponseEntity<Void> subscribe(
      @PathVariable Long topicId,
      Authentication authentication
  ) {
    Long userId = SecurityUtils.getUserId(authentication);
    boolean created = subscriptionService.subscribe(userId, topicId);

    return created
        ? ResponseEntity.status(HttpStatus.CREATED).build()
        : ResponseEntity.noContent().build();
  }

  /**
   * Unsubscribes current user from a topic.
   *
   * @param topicId        topic ID
   * @param authentication current user authentication
   * @return 204 No Content
   */
  @DeleteMapping("/{topicId}")
  public ResponseEntity<Void> unsubscribe(
      @PathVariable Long topicId,
      Authentication authentication
  ) {
    Long userId = SecurityUtils.getUserId(authentication);
    subscriptionService.unsubscribe(userId, topicId);

    return ResponseEntity.noContent().build();
  }
}