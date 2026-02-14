package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.model.SubscriptionDTO;
import com.openclassrooms.mddapi.model.Subscription;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Subscription entities and SubscriptionDTOs.
 */
@Mapper(componentModel = "spring")
public interface SubscriptionMapper {

  /**
   * Converts a Subscription entity to a SubscriptionDTO.
   *
   * @param subscription the subscription entity
   * @return the subscription DTO
   */
  @Mapping(source = "user.id", target = "userId")
  @Mapping(source = "topic.id", target = "topicId")
  SubscriptionDTO toDTO(Subscription subscription);

  /**
   * Converts a list of Subscription entities to a list of SubscriptionDTOs.
   *
   * @param subscriptions the list of subscription entities
   * @return the list of subscription DTOs
   */
  List<SubscriptionDTO> toDTOList(List<Subscription> subscriptions);
}