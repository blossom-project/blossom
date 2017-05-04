package fr.mgargadennec.blossom.core.common.dao;

import fr.mgargadennec.blossom.core.common.entity.AbstractEntity;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public interface CrudDao<ENTITY extends AbstractEntity> extends ReadOnlyDao<ENTITY> {

  ENTITY create(ENTITY toCreate);

  ENTITY update(long id, ENTITY toUpdate);

  void delete(ENTITY toDelete);

  List<ENTITY> create(Collection<ENTITY> toCreates);

  List<ENTITY> update(Map<Long, ENTITY> toUpdates);

}
