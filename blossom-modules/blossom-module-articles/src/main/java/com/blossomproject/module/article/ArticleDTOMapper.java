package com.blossomproject.module.article;

import com.blossomproject.core.common.mapper.AbstractBlossomDTOMapper;
import org.mapstruct.factory.Mappers;

public class ArticleDTOMapper extends AbstractBlossomDTOMapper<Article, ArticleDTO> {
    public ArticleDTOMapper() {
        super(Mappers.getMapper(ArticleMapper.class));
    }
}
