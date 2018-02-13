package com.blossomproject.core.common.dao;

import com.blossomproject.core.common.entity.AbstractEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * An extension of the {@code ReadOnlyDao} which adds CRUD capabilities for a given {@code AbstractEntity}
 *
 * @param <ENTITY> the managed {@link AbstractEntity}
 *
 * @author Maël Gargadennec
 */
public interface CrudDao<ENTITY extends AbstractEntity> extends ReadOnlyDao<ENTITY> {

  /**
   * Creates a new entity
   *
   * @param toCreate the entity to create
   * @return the newly created entity
   */
  ENTITY create(ENTITY toCreate);

  /**
   * Updates an existing entity
   *
   * @param id the identifier of the entity to update.  Throws {@code IllegalArgumentException} if the entity is not found.
   * @param toUpdate the entity to update datas from. Throws {@code IllegalArgumentException} if null.
   * @return the updated entity
   */
  ENTITY update(long id, ENTITY toUpdate);

  /**
   * Deletes an existing entity
   *
   * @param toDelete the entity to delete. Throws {@code IllegalArgumentException} if the provided entity has a {@literal null} id.
   */
  void delete(ENTITY toDelete);

  /**
   * Creates a collection of entities
   *
   * @param toCreates the entities to create
   * @return the newly created entity
   */
  List<ENTITY> create(Collection<ENTITY> toCreates);

  /**
   * <p>Updates a {@code Map} of entities.</p>
   * <p>This method only updates found entities, and does not failed if a provided {@code Entry<Long,ENTITY>} is not found.</p>
   *
   * @param toUpdates the entities to create, each one referenced by it's id
   * @return the updated entities
   */
  List<ENTITY> update(Map<Long, ENTITY> toUpdates);

}
