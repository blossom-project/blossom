package com.blossomproject.module.article;

import com.blossomproject.core.common.mapper.BlossomEntityMapper;
import com.blossomproject.core.common.mapper.BlossomEntityMapperConfig;
import org.mapstruct.Mapper;

@Mapper(config = BlossomEntityMapperConfig.class)
public interface ArticleEntityMapper extends BlossomEntityMapper<Article> {
}
