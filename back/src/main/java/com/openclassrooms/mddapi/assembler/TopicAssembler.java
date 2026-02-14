package com.openclassrooms.mddapi.assembler;

import com.openclassrooms.mddapi.dto.model.TopicDTO;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.service.SubscriptionService;
import com.openclassrooms.mddapi.service.TopicService;
import java.util.List;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Assembler for Topic-related DTOs. Combines data from multiple services to build
 * presentation-ready DTOs.
 */
@Component
@RequiredArgsConstructor
public class TopicAssembler {

  private final TopicService topicService;
  private final SubscriptionService subscriptionService;
  private final TopicMapper topicMapper;

  /**
   * Assembles all topics with subscription status for a specific user.
   *
   * @param userId the ID of the current user
   * @return list of topics with subscription indicators
   */
  public List<TopicDTO> assembleTopicsForUser(Long userId) {
    List<Topic> topics = topicService.getAllTopics();
    Set<Long> subscribedTopicIds = subscriptionService.getSubscribedTopicIds(userId);
    return topicMapper.toDTOListWithSubscription(topics, subscribedTopicIds);
  }
}