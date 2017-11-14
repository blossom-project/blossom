package fr.blossom.module.article;

import fr.blossom.core.common.service.CrudService;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface ArticleService extends CrudService<ArticleDTO> {

    ArticleDTO create(ArticleCreateForm articleCreateForm) throws Exception;

    ArticleDTO update(Long groupId, ArticleUpdateForm articleUpdateForm) throws Exception;
}
