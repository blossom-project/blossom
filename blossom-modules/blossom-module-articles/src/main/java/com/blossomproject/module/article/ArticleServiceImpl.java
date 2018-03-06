package com.blossomproject.module.article;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.event.CreatedEvent;
import com.blossomproject.core.common.event.UpdatedEvent;
import com.blossomproject.core.common.service.AssociationServicePlugin;
import com.blossomproject.core.common.service.GenericCrudServiceImpl;
import com.google.common.base.Preconditions;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class ArticleServiceImpl extends GenericCrudServiceImpl<ArticleDTO, Article> implements ArticleService {

    private final ArticleDao articleDao;

    public ArticleServiceImpl(ArticleDao dao, ArticleDTOMapper mapper, ApplicationEventPublisher publisher, PluginRegistry<AssociationServicePlugin, Class<? extends  AbstractDTO>> associationRegistry) {
        super(dao, mapper, publisher, associationRegistry);
        Preconditions.checkNotNull(dao);
        this.articleDao=dao;
    }

    @Override
    @Transactional
    public ArticleDTO create(ArticleCreateForm articleCreateForm){

        Article articleToCreate = new Article();
        articleToCreate.setName(articleCreateForm.getName());
        articleToCreate.setDescription(articleCreateForm.getDescription());
        articleToCreate.setViewable(false);
        ArticleDTO savedArticle = this.mapper.mapEntity(this.crudDao.create(articleToCreate));

        this.publisher.publishEvent(new CreatedEvent<ArticleDTO>(this, savedArticle));

        return savedArticle;
    }

    @Override
    @Transactional
    public ArticleDTO update(Long articleId, ArticleUpdateForm articleUpdateForm) {

        ArticleDTO articleToUpdate = this.getOne(articleId);
        articleToUpdate.setName(articleUpdateForm.getName());
        articleToUpdate.setDescription(articleUpdateForm.getDescription());
        articleToUpdate.setContent(articleUpdateForm.getContent());
        articleToUpdate.setViewable(articleUpdateForm.isViewable());

        ArticleDTO savedArticle = this.update(articleId,articleToUpdate);

        this.publisher.publishEvent(new UpdatedEvent<ArticleDTO>(this, savedArticle));

        return savedArticle;
    }

    @Override
    public List<ArticleDTO> getAllOrderedByCreationDate() {
        return this.mapper.mapEntities(this.articleDao.getAllOrderedByCreationDate());
    }
}
