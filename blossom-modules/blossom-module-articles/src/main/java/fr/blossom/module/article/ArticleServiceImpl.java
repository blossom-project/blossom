package fr.blossom.module.article;

import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.event.CreatedEvent;
import fr.blossom.core.common.event.UpdatedEvent;
import fr.blossom.core.common.service.AssociationServicePlugin;
import fr.blossom.core.common.service.GenericCrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class ArticleServiceImpl extends GenericCrudServiceImpl<ArticleDTO, Article> implements ArticleService {
    private final static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    public ArticleServiceImpl(ArticleDao dao, ArticleDTOMapper mapper, ApplicationEventPublisher publisher, PluginRegistry<AssociationServicePlugin, Class<? extends  AbstractDTO>> associationRegistry) {
        super(dao, mapper, publisher, associationRegistry);
    }

    @Override
    @Transactional
    public ArticleDTO create(ArticleCreateForm articleCreateForm) throws Exception {

        Article articleToCreate = new Article();
        articleToCreate.setName(articleCreateForm.getName());

        ArticleDTO savedArticle = this.mapper.mapEntity(this.crudDao.create(articleToCreate));

        this.publisher.publishEvent(new CreatedEvent<ArticleDTO>(this, savedArticle));

        return savedArticle;
    }

    @Override
    @Transactional
    public ArticleDTO update(Long articleId, ArticleUpdateForm articleUpdateForm) throws Exception {

        Article articleToUpdate = new Article();
        articleToUpdate.setName(articleUpdateForm.getName());

        ArticleDTO savedArticle = this.mapper.mapEntity(this.crudDao.update(articleId, articleToUpdate));

        this.publisher.publishEvent(new UpdatedEvent<ArticleDTO>(this, savedArticle));

        return savedArticle;
    }
}
