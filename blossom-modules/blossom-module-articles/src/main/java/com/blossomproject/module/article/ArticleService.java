package com.blossomproject.module.article;

import com.blossomproject.core.common.service.CrudService;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
public interface ArticleService extends CrudService<ArticleDTO> {

    ArticleDTO create(ArticleCreateForm articleCreateForm);

    ArticleDTO update(Long groupId, ArticleUpdateForm articleUpdateForm);

}
