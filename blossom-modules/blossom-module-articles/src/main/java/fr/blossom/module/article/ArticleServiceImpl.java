package fr.blossom.module.article;

import fr.blossom.core.common.event.CreatedEvent;
import fr.blossom.core.common.event.UpdatedEvent;
import fr.blossom.core.common.service.GenericCrudServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public class ArticleServiceImpl extends GenericCrudServiceImpl<ArticleDTO, Article> implements ArticleService {
    private final static Logger logger = LoggerFactory.getLogger(ArticleServiceImpl.class);

    public ArticleServiceImpl(ArticleDao dao, ArticleDTOMapper mapper, ApplicationEventPublisher publisher) {
        super(dao, mapper, publisher);
    }

    @Override
    public ArticleDTO create(ArticleCreateForm articleCreateForm) throws Exception {

        Article articleToCreate = new Article();
        articleToCreate.setName(articleCreateForm.getName());

        ArticleDTO savedArticle = this.mapper.mapEntity(this.dao.create(articleToCreate));

        this.publisher.publishEvent(new CreatedEvent<ArticleDTO>(this, savedArticle));

        return savedArticle;
    }

    @Override
    public ArticleDTO update(Long articleId, ArticleUpdateForm articleUpdateForm) throws Exception {

        Article articleToUpdate = new Article();
        articleToUpdate.setName(articleUpdateForm.getName());

        ArticleDTO savedArticle = this.mapper.mapEntity(this.dao.update(articleId, articleToUpdate));

        this.publisher.publishEvent(new UpdatedEvent<ArticleDTO>(this, savedArticle));

        return savedArticle;
    }
}
