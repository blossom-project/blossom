package com.blossomproject.module.article;

import com.blossomproject.core.common.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Maël Gargadennnec on 03/05/2017.
 */
@Repository
public interface ArticleRepository extends CrudRepository<Article> {
}
