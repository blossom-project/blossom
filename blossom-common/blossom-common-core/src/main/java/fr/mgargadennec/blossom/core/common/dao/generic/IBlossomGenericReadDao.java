package fr.mgargadennec.blossom.core.common.dao.generic;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

public interface IBlossomGenericReadDao<T extends BlossomAbstractEntity> {

  Page<T> getAll(Pageable pageable);

  T get(Long id);

  boolean exists(Long id);

  boolean existsOrFail(Long id);

}