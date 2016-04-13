package fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.tools.Pair;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;

public class BlossomIndexationHistoryMap {

  private Map<Class<? extends BlossomAbstractEntity>, Map<Long, Pair<BlossomRevisionEntity, BlossomAbstractEntity>>> indexMap;

  public BlossomIndexationHistoryMap() {
    indexMap = new HashMap<Class<? extends BlossomAbstractEntity>, Map<Long, Pair<BlossomRevisionEntity, BlossomAbstractEntity>>>();
  }

  public void push(BlossomRevisionEntity revision, BlossomAbstractEntity entity) {
    if (entity.getId() == null) {
      return;
    }

    @SuppressWarnings("unchecked")
    Class<? extends BlossomAbstractEntity> entityClazz = entity.getClass();

    Map<Long, Pair<BlossomRevisionEntity, BlossomAbstractEntity>> entityClazzMap;

    if (indexMap.containsKey(entityClazz)) {
      entityClazzMap = indexMap.get(entityClazz);
    } else {
      entityClazzMap = new HashMap<Long, Pair<BlossomRevisionEntity, BlossomAbstractEntity>>();
    }

    entityClazzMap.put(entity.getId(), new Pair<BlossomRevisionEntity, BlossomAbstractEntity>(revision, entity));

    indexMap.put(entityClazz, entityClazzMap);
  }

  /**
   * Retourne et supprime une entite de la map
   *
   * @param entityClazz
   * @param entityId
   * @return
   */
  public Pair<BlossomRevisionEntity, BlossomAbstractEntity> pull(Class<? extends BlossomAbstractEntity> entityClazz,
      Long entityId) {
    if (indexMap.containsKey(entityClazz)) {
      Map<Long, Pair<BlossomRevisionEntity, BlossomAbstractEntity>> entityClazzMap;
      entityClazzMap = indexMap.get(entityClazz);

      return entityClazzMap.remove(entityId);
    }

    return null;
  }

  /**
   * @return la liste des entites de la map
   */
  public List<Pair<BlossomRevisionEntity, BlossomAbstractEntity>> getRemainingEntityList() {
    List<Pair<BlossomRevisionEntity, BlossomAbstractEntity>> resultList = new ArrayList<Pair<BlossomRevisionEntity, BlossomAbstractEntity>>();

    for (Map<Long, Pair<BlossomRevisionEntity, BlossomAbstractEntity>> entityClazzMap : indexMap.values()) {
      resultList.addAll(entityClazzMap.values());
    }

    return resultList;
  }
}
