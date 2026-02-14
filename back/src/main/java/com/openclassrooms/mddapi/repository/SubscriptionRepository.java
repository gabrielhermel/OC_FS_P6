package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for Subscription entity database operations.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  /**
   * Finds all subscriptions for a specific user.
   *
   * @param userId user ID
   * @return list of subscriptions
   */
  List<Subscription> findByUserId(Long userId);

  /**
   * Checks if user is subscribed to topic.
   *
   * @param userId  user ID
   * @param topicId topic ID
   * @return true if subscribed
   */
  boolean existsByUserIdAndTopicId(Long userId, Long topicId);

  /**
   * Deletes subscription by user and topic.
   *
   * @param userId  user ID
   * @param topicId topic ID
   */
  void deleteByUserIdAndTopicId(Long userId, Long topicId);
}