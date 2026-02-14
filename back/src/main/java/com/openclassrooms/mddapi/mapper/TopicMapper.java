package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.model.TopicDTO;
import com.openclassrooms.mddapi.model.Topic;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Topic entities and TopicDTOs.
 */
@Mapper(componentModel = "spring")
public interface TopicMapper {

  /**
   * Converts a Topic entity to a TopicDTO with subscription status.
   *
   * @param topic      the topic entity
   * @param subscribed whether the user is subscribed to this topic
   * @return the topic DTO
   */
  @Mapping(target = "subscribed", expression = "java(subscribed)")
  TopicDTO toDTO(Topic topic, Boolean subscribed);

  /**
   * Converts a list of Topic entities to TopicDTOs with subscription status.
   *
   * @param topics             the list of topic entities
   * @param subscribedTopicIds set of topic IDs the user is subscribed to
   * @return the list of topic DTOs with subscription status
   */
  default List<TopicDTO> toDTOListWithSubscription(List<Topic> topics,
      Set<Long> subscribedTopicIds) {
    return topics.stream()
        .map(topic -> toDTO(topic, subscribedTopicIds.contains(topic.getId())))
        .toList();
  }
}