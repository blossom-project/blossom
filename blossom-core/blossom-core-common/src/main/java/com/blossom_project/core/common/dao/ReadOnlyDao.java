package com.blossom_project.core.common.dao;

import com.blossom_project.core.common.entity.AbstractEntity;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * Basic DAO interface for an {@code AbstractEntity} for read-only operations only.
 * @author Maël Gargadennec
 */
public interface ReadOnlyDao<ENTITY extends AbstractEntity> {

  /**
   * <p>Retrieves the complete {@code List} of entities from the underlying datasource.</p>
   *
   * <p>Be careful : don't use this method on large datasets ! Use {@link ReadOnlyDao#getAll(Pageable
   * pageable)} instead.</p>
   *
   * @return the complete list of entities
   */
  List<ENTITY> getAll();

  /**
   * Retrieves a paginated subset of the entities from the underlying datasource
   *
   * @param pageable a spring data {@link Pageable}. Throws an {@code IllegalArgumentException} if
   * null.
   * @return the asked {@link Page} of entities
   */
  Page<ENTITY> getAll(Pageable pageable);

  /**
   * Retrieves an id-filtered subset of the entities from the underlying datasource
   *
   * @param ids the list of entities ids you want to retrieve
   * @return the complete list of entities
   */
  List<ENTITY> getAll(List<Long> ids);

  /**
   * Retrieves a single entity
   *
   * Be careful : don't use this method on large datasets ! Use {@link ReadOnlyDao#getAll(Pageable
   * pageable)} instead.
   *
   * @param  id the entity id
   * @return the complete list of entities
   */
  ENTITY getOne(long id);

}
