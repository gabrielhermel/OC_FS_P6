package com.openclassrooms.mddapi.assembler;

import com.openclassrooms.mddapi.dto.model.TopicDTO;
import com.openclassrooms.mddapi.dto.response.UserProfileResponse;
import com.openclassrooms.mddapi.mapper.TopicMapper;
import com.openclassrooms.mddapi.model.Subscription;
import com.openclassrooms.mddapi.model.User;
import com.openclassrooms.mddapi.service.SubscriptionService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Assembler for User-related DTOs. Combines data from multiple services to build presentation-ready
 * DTOs.
 */
@Component
@RequiredArgsConstructor
public class UserAssembler {

  private final SubscriptionService subscriptionService;
  private final TopicMapper topicMapper;

  /**
   * Assembles user profile response with subscriptions.
   *
   * @param user the user entity
   * @return user profile response with subscription list
   */
  @Transactional(readOnly = true)
  public UserProfileResponse assembleUserProfile(User user) {
    List<Subscription> subscriptions = subscriptionService.getUserSubscriptions(user.getId());

    List<TopicDTO> subscriptionDTOs = subscriptions.stream()
        .map(subscription -> topicMapper.toDTO(subscription.getTopic(), true))
        .toList();

    return new UserProfileResponse(
        user.getId(),
        user.getUsername(),
        user.getEmail(),
        subscriptionDTOs
    );
  }
}