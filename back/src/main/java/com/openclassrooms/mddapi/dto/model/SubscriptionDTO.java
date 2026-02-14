package com.openclassrooms.mddapi.dto.model;

/**
 * DTO for Subscription information.
 */
public record SubscriptionDTO(
    Long id,
    Long userId,
    Long topicId
) {

}