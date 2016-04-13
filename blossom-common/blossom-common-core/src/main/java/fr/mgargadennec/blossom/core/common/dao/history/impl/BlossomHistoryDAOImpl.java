package fr.mgargadennec.blossom.core.common.dao.history.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.envers.AuditReader;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.internal.tools.Triple;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;

public class BlossomHistoryDAOImpl implements IBlossomHistoryDAO {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlossomHistoryDAOImpl.class);

  AuditReader auditReader;

  public BlossomHistoryDAOImpl(AuditReader auditReader) {
    this.auditReader = auditReader;
  }

  public Map<RevisionType, List<BlossomAbstractEntity>> getEntityListGroupByRevisionType(Number revision) {
    Map<RevisionType, List<BlossomAbstractEntity>> resultMap = new HashMap<RevisionType, List<BlossomAbstractEntity>>();

    Map<RevisionType, List<Object>> objectMap = auditReader.getCrossTypeRevisionChangesReader()
        .findEntitiesGroupByRevisionType(revision);

    for (Map.Entry<RevisionType, List<Object>> entry : objectMap.entrySet()) {
      List<BlossomAbstractEntity> entityList = new ArrayList<BlossomAbstractEntity>();
      for (Object entity : entry.getValue()) {
        try {
          entityList.add((BlossomAbstractEntity) entity);
        } catch (ClassCastException e) {
          LOGGER.error("Error while casting {} to {}", entity.getClass().getName(),
              BlossomAbstractEntity.class.getName(), e);
        }
      }
      resultMap.put(entry.getKey(), entityList);
    }

    return resultMap;
  }

  public <A extends BlossomAbstractEntity> List<Triple<A, BlossomRevisionEntity, RevisionType>> getRevisionListOfEntity(
      Class<A> blossomEntityClazz, Long id) {

    List<Triple<A, BlossomRevisionEntity, RevisionType>> resultList = new ArrayList<Triple<A, BlossomRevisionEntity, RevisionType>>();

    AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(blossomEntityClazz, false, true);
    query.add(AuditEntity.property("id").eq(id));
    @SuppressWarnings("unchecked")
    List<Object[]> objectList = query.getResultList();

    Triple<A, BlossomRevisionEntity, RevisionType> triple;
    for (Object[] object : objectList) {
      try {
        triple = new Triple<A, BlossomRevisionEntity, RevisionType>((A) object[0], (BlossomRevisionEntity) object[1],
            (RevisionType) object[2]);
        resultList.add(triple);
      } catch (ClassCastException e) {
        LOGGER.error("Error while casting {} to Triple<{}, {}, {}>", object.getClass().getName(),
            BlossomAbstractEntity.class.getName(), BlossomRevisionEntity.class.getName(), RevisionType.class.getName(), e);
      }
    }

    return resultList;
  }

  public <A extends BlossomAbstractEntity> A getEntityAtPreviousRevision(Class<A> blossomEntityClazz, Long id, Number revision) {

    AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(blossomEntityClazz, false, true);
    query.addProjection(AuditEntity.revisionNumber().max());
    query.add(AuditEntity.property("id").eq(id));
    query.add(AuditEntity.revisionNumber().lt(revision.intValue()));

    Number previousRevision = (Number) query.getSingleResult();

    if (previousRevision != null) {
      return auditReader.find(blossomEntityClazz, id, previousRevision);
    }

    return null;
  }

  public <A extends BlossomAbstractEntity> A getEntityAtNextRevision(Class<A> blossomEntityClazz, Long id, Number revision) {

    AuditQuery query = auditReader.createQuery().forRevisionsOfEntity(blossomEntityClazz, false, true);
    query.addProjection(AuditEntity.revisionNumber().max());
    query.add(AuditEntity.property("id").eq(id));
    query.add(AuditEntity.revisionNumber().gt(revision.intValue()));

    Number previousRevision = (Number) query.getSingleResult();

    if (previousRevision != null) {
      return auditReader.find(blossomEntityClazz, id, previousRevision);
    }

    return null;
  }
}
