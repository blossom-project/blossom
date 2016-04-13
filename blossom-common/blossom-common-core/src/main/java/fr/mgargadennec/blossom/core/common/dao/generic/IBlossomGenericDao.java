package fr.mgargadennec.blossom.core.common.dao.generic;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;

public interface IBlossomGenericDao<T extends BlossomAbstractEntity> extends IBlossomGenericReadDao<T> {

  T create(T po);

  T update(T po);

  void delete(Long id);

}