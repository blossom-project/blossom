package com.blossomproject.module.article;

import com.blossomproject.core.common.dao.GenericCrudDaoImpl;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class ArticleDaoImpl extends GenericCrudDaoImpl<Article> implements ArticleDao {
  public ArticleDaoImpl(ArticleRepository repository) {
    super(repository);
  }

  @Override
  protected Article updateEntity(Article originalEntity, Article modifiedEntity) {
    originalEntity.setName(modifiedEntity.getName());
    return originalEntity;
  }

}
