package com.blossomproject.module.article;

import com.blossomproject.core.common.mapper.AbstractDTOMapper;

public class ArticleDTOMapper extends AbstractDTOMapper<Article, ArticleDTO> {

  @Override
  public ArticleDTO mapEntity(Article entity) {
    if (entity == null) {
      return null;
    }

    ArticleDTO dto = new ArticleDTO();
    mapEntityCommonFields(dto, entity);
    dto.setName(entity.getName());
    dto.setSummary(entity.getSummary());
    dto.setContent(entity.getContent());
    dto.setStatus(entity.getStatus());
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
    entity.setSummary(dto.getSummary());
    entity.setContent(dto.getContent());
    entity.setStatus(dto.getStatus());
    return entity;
  }
}
