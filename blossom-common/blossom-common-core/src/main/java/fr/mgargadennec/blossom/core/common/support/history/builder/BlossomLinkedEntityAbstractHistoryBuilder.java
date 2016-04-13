package fr.mgargadennec.blossom.core.common.support.history.builder;

import javax.persistence.Entity;

import org.hibernate.envers.RevisionType;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryDTO;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryLinkedEntityDTO;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryTechnicalException;

public abstract class BlossomLinkedEntityAbstractHistoryBuilder<M extends BlossomAbstractEntity, L extends BlossomAbstractEntity>
extends BlossomAbstractHistoryBuilder {

  public BlossomLinkedEntityAbstractHistoryBuilder(IBlossomEntityDiffBuilder diffBuilder) {
    super(diffBuilder);
  }

  @Override
  protected <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> doBuildDTOBase(RevisionType revisionType,
      BlossomRevisionEntity revision, E beforeEntity, E afterEntity) throws BlossomHistoryTechnicalException {

	  BlossomHistoryLinkedEntityDTO<E, L> historyDTO = new BlossomHistoryLinkedEntityDTO<E, L>();
    historyDTO.setRevisionType(revisionType);
    historyDTO.setRevisionId(revision.getId());
    historyDTO.setRevisionTimestamp(revision.getTimestamp());
    historyDTO.setUserModification(revision.getUser());
    historyDTO.setBeforeEntity(beforeEntity);
    historyDTO.setAfterEntity(afterEntity);

    if (beforeEntity == null) {
      historyDTO.setDiff(diffBuilder.computeDiffWithoutBefore(afterEntity));
    } else if (afterEntity == null) {
      historyDTO.setDiff(diffBuilder.computeDiffWithoutAfter(beforeEntity));
    } else {
      historyDTO.setDiff(diffBuilder.computeDiff(beforeEntity, afterEntity));
    }

    BlossomAbstractEntity entityToUse = null;

    switch (revisionType) {
    case ADD:
    case MOD:
      entityToUse = afterEntity;
      break;
    case DEL:
      entityToUse = beforeEntity;
      break;
    default:
      break;
    }

    historyDTO.setEntityName(entityToUse.getClass().getAnnotation(Entity.class).name());
    historyDTO.setEntityClass(entityToUse.getClass().getCanonicalName());
    historyDTO.setEntityId(entityToUse.getId());
    historyDTO.setUserCreation(entityToUse.getUserCreation());

    L linkedEntity = getLinkedEntity(revision, entityToUse);
    historyDTO.setLinkedClass(getLinkedEntityClass(revision, entityToUse).getCanonicalName());
    historyDTO.setLinkedEntity(linkedEntity);
    historyDTO.setLinkedName(getLinkedEntityClass(revision, entityToUse).getAnnotation(Entity.class).name());
    historyDTO.setLinkedEntityId(linkedEntity != null ? linkedEntity.getId().toString() : null);

    return historyDTO;
  }

  protected abstract <E extends BlossomAbstractEntity> Class<L> getLinkedEntityClass(BlossomRevisionEntity revision, E entity);

  protected abstract <E extends BlossomAbstractEntity> L getLinkedEntity(BlossomRevisionEntity revision, E entity);

}
