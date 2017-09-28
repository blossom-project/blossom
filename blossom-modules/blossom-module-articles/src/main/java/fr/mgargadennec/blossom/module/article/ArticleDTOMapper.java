package fr.mgargadennec.blossom.module.article;

import fr.mgargadennec.blossom.core.common.mapper.AbstractDTOMapper;

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
