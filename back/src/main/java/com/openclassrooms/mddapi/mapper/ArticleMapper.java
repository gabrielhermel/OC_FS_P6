package com.openclassrooms.mddapi.mapper;

import com.openclassrooms.mddapi.dto.model.ArticleDTO;
import com.openclassrooms.mddapi.model.Article;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * MapStruct mapper for converting between Article entities and DTOs.
 */
@Mapper(componentModel = "spring")
public interface ArticleMapper {

  /**
   * Converts an Article entity to ArticleDTO.
   *
   * @param article the article entity
   * @return the article DTO
   */
  @Mapping(source = "topic.id", target = "topicId")
  @Mapping(source = "topic.name", target = "topicName")
  @Mapping(source = "author.id", target = "authorId")
  @Mapping(source = "author.username", target = "authorName")
  ArticleDTO toDTO(Article article);

  /**
   * Converts a list of Article entities to ArticleDTOs.
   *
   * @param articles the list of article entities
   * @return the list of article DTOs
   */
  List<ArticleDTO> toDTOList(List<Article> articles);
}