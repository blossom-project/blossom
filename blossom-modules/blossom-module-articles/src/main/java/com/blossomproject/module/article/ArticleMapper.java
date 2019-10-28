package com.blossomproject.module.article;

import com.blossomproject.core.common.mapper.BlossomMapper;
import org.mapstruct.Mapper;

@Mapper
public interface ArticleMapper extends BlossomMapper<Article,ArticleDTO> {
}
