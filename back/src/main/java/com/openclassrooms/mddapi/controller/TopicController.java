package com.openclassrooms.mddapi.controller;

import com.openclassrooms.mddapi.dto.model.TopicDTO;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.service.TopicService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
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

  private final TopicService topicService;
  private final TopicMapper topicMapper;

  /**
   * Retrieves all available topics.
   *
   * @return list of all topics
   */
  @GetMapping
  public ResponseEntity<List<TopicDTO>> getAllTopics() {
    return ResponseEntity.ok(topicMapper.toDTOList(topicService.getAllTopics()));
  }
}