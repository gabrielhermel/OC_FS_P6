package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.UserDTO;
import com.openclassrooms.mddapi.model.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

/**
 * MapStruct mapper for converting between User entities and UserDTOs.
 */
@Mapper
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);

  /**
   * Converts a User entity to a UserDTO.
   *
   * @param user the user entity
   * @return the user DTO
   */
  UserDTO toDTO(User user);

  /**
   * Converts a list of User entities to a list of UserDTOs.
   *
   * @param users the list of user entities
   * @return the list of user DTOs
   */
  List<UserDTO> toDTOList(List<User> users);
}