package com.blossom_project.module.article;

import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.event.CreatedEvent;
import com.blossom_project.core.common.event.UpdatedEvent;
import com.blossom_project.core.common.service.AssociationServicePlugin;
import com.blossom_project.core.common.service.GenericCrudServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class ArticleServiceImpl extends GenericCrudServiceImpl<ArticleDTO, Article> implements ArticleService {

    public ArticleServiceImpl(ArticleDao dao, ArticleDTOMapper mapper, ApplicationEventPublisher publisher, PluginRegistry<AssociationServicePlugin, Class<? extends  AbstractDTO>> associationRegistry) {
        super(dao, mapper, publisher, associationRegistry);
    }

    @Override
    @Transactional
    public ArticleDTO create(ArticleCreateForm articleCreateForm){

        Article articleToCreate = new Article();
        articleToCreate.setName(articleCreateForm.getName());

        ArticleDTO savedArticle = this.mapper.mapEntity(this.crudDao.create(articleToCreate));

        this.publisher.publishEvent(new CreatedEvent<ArticleDTO>(this, savedArticle));

        return savedArticle;
    }

    @Override
    @Transactional
    public ArticleDTO update(Long articleId, ArticleUpdateForm articleUpdateForm) {

        Article articleToUpdate = new Article();
        articleToUpdate.setName(articleUpdateForm.getName());

        ArticleDTO savedArticle = this.mapper.mapEntity(this.crudDao.update(articleId, articleToUpdate));

        this.publisher.publishEvent(new UpdatedEvent<ArticleDTO>(this, savedArticle));

        return savedArticle;
    }
}
