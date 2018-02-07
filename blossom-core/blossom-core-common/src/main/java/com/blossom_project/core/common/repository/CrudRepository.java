package com.blossom_project.core.common.repository;

import com.blossom_project.core.common.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Base repository to manage any {@code AbstractEntity}
 *
 * @param <ENTITY> the managed {@code AbstractEntity}
 * @author Maël Gargadennnec
 */
@NoRepositoryBean
public interface CrudRepository<ENTITY extends AbstractEntity> extends JpaRepository<ENTITY, Long>, QuerydslPredicateExecutor<ENTITY> {

}
