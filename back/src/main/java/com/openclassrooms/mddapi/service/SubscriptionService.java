package com.openclassrooms.mddapi.service;

import com.openclassrooms.mddapi.exception.ResourceNotFoundException;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.Topic;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.repository.SubscriptionRepository;
import com.openclassrooms.mddapi.repository.TopicRepository;
import com.openclassrooms.mddapi.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service for subscription-related business logic.
 */
@Service
@RequiredArgsConstructor
public class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final UserRepository userRepository;
  private final TopicRepository topicRepository;

  /**
   * Subscribes a user to a topic. Succeeds if already subscribed (idempotent).
   *
   * @param userId  user ID
   * @param topicId topic ID
   * @return true if newly subscribed, false if already subscribed
   */
  @Transactional
  public boolean subscribe(Long userId, Long topicId) {
    // Validate user and topic exist
    User user = userRepository.findById(userId)
        .orElseThrow(() -> new ResourceNotFoundException("Utilisateur non trouvé"));
    Topic topic = topicRepository.findById(topicId)
        .orElseThrow(() -> new ResourceNotFoundException("Thème non trouvé"));

    // Check if already subscribed
    if (subscriptionRepository.existsByUserIdAndTopicId(userId, topicId)) {
      return false;
    }

    Subscription subscription = new Subscription();
    subscription.setUser(user);
    subscription.setTopic(topic);
    subscriptionRepository.save(subscription);
    return true;
  }

  /**
   * Unsubscribes a user from a topic.
   *
   * @param userId  user ID
   * @param topicId topic ID
   */
  @Transactional
  public void unsubscribe(Long userId, Long topicId) {
    if (!subscriptionRepository.existsByUserIdAndTopicId(userId, topicId)) {
      throw new ResourceNotFoundException("Abonnement non trouvé");
    }
    subscriptionRepository.deleteByUserIdAndTopicId(userId, topicId);
  }

  /**
   * Gets all subscriptions for a user.
   *
   * @param userId user ID
   * @return list of subscriptions
   */
  public List<Subscription> getUserSubscriptions(Long userId) {
    return subscriptionRepository.findByUserId(userId);
  }
}