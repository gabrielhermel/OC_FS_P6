package com.openclassrooms.mddapi.dto.response;

import com.openclassrooms.mddapi.dto.model.TopicDTO;
import java.util.List;

/**
 * DTO for user profile response including subscriptions.
 */
public record UserProfileResponse(
    Long id,
    String username,
    String email,
    List<TopicDTO> subscriptions
) {

}