package com.blossomproject.module.article;

import com.blossomproject.core.common.dao.GenericCrudDaoImpl;
import com.google.common.base.Preconditions;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class ArticleDaoImpl extends GenericCrudDaoImpl<Article> implements ArticleDao {

  private final ArticleRepository articleRepository;
  public ArticleDaoImpl(ArticleRepository repository) {
    super(repository);
    Preconditions.checkNotNull(repository);
    this.articleRepository=repository;
  }

  @Override
  protected Article updateEntity(Article originalEntity, Article modifiedEntity) {
    originalEntity.setName(modifiedEntity.getName());
    originalEntity.setDescription(modifiedEntity.getDescription());
    originalEntity.setContent(modifiedEntity.getContent());
    originalEntity.setViewable(modifiedEntity.isViewable());
    return originalEntity;
  }

  @Override
  public List<Article> getAllOrderedByCreationDate() {
    return this.articleRepository.findAllByOrderByCreationDateDesc();
  }
}
