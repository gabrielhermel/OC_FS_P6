package com.openclassrooms.mddapi.repository;

import com.openclassrooms.mddapi.model.Subscription;
import java.util.List;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository for Subscription entity database operations.
 */
@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

  /**
   * Finds all subscriptions for a specific user. Uses lazy loading for Topic (use when only topic
   * IDs are needed).
   *
   * @param userId user ID
   * @return list of subscriptions
   */
  List<Subscription> findByUserId(Long userId);

  /**
   * Finds all subscriptions for a specific user with topics eagerly loaded (uses @EntityGraph to
   * avoid LazyInitializationException). Use this when you need full topic data (name, description,
   * etc.).
   *
   * @param userId user ID
   * @return list of subscriptions with eagerly loaded topics
   */
  @EntityGraph(attributePaths = "topic")
  @Query("SELECT s FROM Subscription s WHERE s.user.id = :userId")
  List<Subscription> findByUserIdWithTopics(@Param("userId") Long userId);

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