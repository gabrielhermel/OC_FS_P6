package com.openclassrooms.mddapi.dto.model;

/**
 * DTO for Topic responses. Represents topic data sent to clients via the API.
 */
public record TopicDTO(
    Long id,
    String name,
    String description
) {

}