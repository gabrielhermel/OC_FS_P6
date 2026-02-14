package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.assembler.TopicAssembler;
import com.openclassrooms.mddapi.dto.model.TopicDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for topic-related endpoints.
 */
@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
public class TopicController {

  private final TopicAssembler topicAssembler;

  /**
   * Retrieves all available topics with subscription status for current user.
   *
   * @param authentication current user authentication
   * @return list of all topics with subscription indicators
   */
  @GetMapping
  public ResponseEntity<List<TopicDTO>> getAllTopics(Authentication authentication) {
    Long userId = Long.parseLong(authentication.getName());
    return ResponseEntity.ok(topicAssembler.assembleTopicsForUser(userId));
  }
}