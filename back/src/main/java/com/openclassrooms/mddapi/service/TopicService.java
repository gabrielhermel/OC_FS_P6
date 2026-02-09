package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.repository.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service layer for Topic operations. Handles business logic related to topics.
 */
@Service
@RequiredArgsConstructor
public class TopicService {

  private final TopicRepository topicRepository;

  /**
   * Retrieves all topics from the database.
   *
   * @return list of all topics
   */
  public List<Topic> getAllTopics() {
    return topicRepository.findAll();
  }
}