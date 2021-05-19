package com.blossomproject.module.article;

import com.blossomproject.core.common.dao.BlossomGenericCrudDaoImpl;
import com.google.common.base.Preconditions;
import org.mapstruct.factory.Mappers;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class ArticleDaoImpl extends BlossomGenericCrudDaoImpl<Article> implements ArticleDao {

    private final ArticleRepository articleRepository;

    public ArticleDaoImpl(ArticleRepository repository) {
        super(repository, Mappers.getMapper(ArticleEntityMapper.class));
        Preconditions.checkNotNull(repository);
        this.articleRepository = repository;
    }

}
