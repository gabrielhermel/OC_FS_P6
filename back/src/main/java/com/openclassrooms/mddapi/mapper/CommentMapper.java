package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.model.CommentDTO;
import com.openclassrooms.mddapi.model.Comment;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Comment entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface CommentMapper {

  /**
   * Converts a Comment entity to CommentDTO.
   *
   * @param comment the comment entity
   * @return the comment DTO
   */
  @Mapping(source = "author.id", target = "authorId")
  @Mapping(source = "author.username", target = "authorName")
  CommentDTO toDTO(Comment comment);

  /**
   * Converts a list of Comment entities to CommentDTOs.
   *
   * @param comments the list of comment entities
   * @return the list of comment DTOs
   */
  List<CommentDTO> toDTOList(List<Comment> comments);
}