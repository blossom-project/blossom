package com.blossom_project.module.article;

import com.blossom_project.core.common.mapper.AbstractDTOMapper;

public class ArticleDTOMapper extends AbstractDTOMapper<Article, ArticleDTO> {

  @Override
  public ArticleDTO mapEntity(Article entity) {
    if (entity == null) {
      return null;
    }

    ArticleDTO dto = new ArticleDTO();
    mapEntityCommonFields(dto, entity);
    dto.setName(entity.getName());
    return dto;
  }

  @Override
  public Article mapDto(ArticleDTO dto) {
    if (dto == null) {
      return null;
    }

    Article entity = new Article();
    mapDtoCommonFields(entity, dto);
    entity.setName(dto.getName());
    return entity;
  }
}
