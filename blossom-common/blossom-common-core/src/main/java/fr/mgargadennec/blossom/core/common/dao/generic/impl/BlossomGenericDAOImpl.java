package fr.mgargadennec.blossom.core.common.dao.generic.impl;

import javax.persistence.EntityNotFoundException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

import fr.mgargadennec.blossom.core.common.dao.generic.IBlossomGenericDao;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.repository.generic.BlossomJpaRepository;

public abstract class BlossomGenericDAOImpl<T extends BlossomAbstractEntity> implements IBlossomGenericDao<T> {

  protected BlossomJpaRepository<T> repository;

  public BlossomGenericDAOImpl(BlossomJpaRepository<T> repository) {
    this.repository = repository;
  }

  public Page<T> getAll(Pageable pageable) {
    return repository.findAll(pageable);
  }

  public T get(Long id) {
    T result = repository.findOne(id);
    if (result == null) {
      throw new EntityNotFoundException();
    }
    return result;
  }

  public boolean exists(Long id) {
    return repository.exists(id);
  }

  public boolean existsOrFail(Long id) {
    if (repository.exists(id)) {
      return true;
    }
    throw new EntityNotFoundException();
  }

  @Transactional
  public T create(T po) {
    Preconditions.checkNotNull(po);
    Preconditions.checkState(po.getId() == null, "Can't create if ID is already defined.");
    return repository.save(po);
  }

  @Transactional
  public T update(T po) {
    Preconditions.checkNotNull(po);
    Preconditions.checkState(po.getId() != null, "Can't update if ID is not already defined.");
    return repository.save(po);
  }

  @Transactional
  public void delete(Long id) {
    repository.delete(id);
  }

}