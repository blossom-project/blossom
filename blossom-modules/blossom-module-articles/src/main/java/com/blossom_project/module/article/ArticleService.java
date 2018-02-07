package com.blossom_project.module.article;

import com.blossom_project.core.common.service.CrudService;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface ArticleService extends CrudService<ArticleDTO> {

    ArticleDTO create(ArticleCreateForm articleCreateForm);

    ArticleDTO update(Long groupId, ArticleUpdateForm articleUpdateForm);
}
