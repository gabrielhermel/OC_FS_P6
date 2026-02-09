package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.TopicDTO;
import com.openclassrooms.mddapi.model.Topic;
import java.util.List;
import org.mapstruct.Mapper;

/**
 * MapStruct mapper for converting between Topic entities and TopicDTOs.
 */
@Mapper(componentModel = "spring")
public interface TopicMapper {

  /**
   * Converts a Topic entity to a TopicDTO.
   *
   * @param topic the topic entity
   * @return the topic DTO
   */
  TopicDTO toDTO(Topic topic);

  /**
   * Converts a list of Topic entities to a list of TopicDTOs.
   *
   * @param topics the list of topic entities
   * @return the list of topic DTOs
   */
  List<TopicDTO> toDTOList(List<Topic> topics);
}