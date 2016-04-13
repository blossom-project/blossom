package fr.mgargadennec.blossom.core.common.support.history.builder;

import org.hibernate.envers.RevisionType;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryDTO;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryIncoherentDataException;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryIncompleteDataException;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryTechnicalException;

public abstract class BlossomAbstractHistoryBuilder implements IBlossomHistoryBuilder {

  protected IBlossomEntityDiffBuilder diffBuilder;

  public BlossomAbstractHistoryBuilder(IBlossomEntityDiffBuilder diffBuilder) {
    this.diffBuilder = diffBuilder;
  }

  public <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTOfromADD(BlossomRevisionEntity revision, E afterEntity)
      throws BlossomHistoryIncompleteDataException, BlossomHistoryTechnicalException {
    return buildDTObase(RevisionType.ADD, revision, null, afterEntity);
  }

  public <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTOfromMOD(BlossomRevisionEntity revision, E beforeEntity,
      E afterEntity) throws BlossomHistoryIncompleteDataException, BlossomHistoryIncoherentDataException, BlossomHistoryTechnicalException {

	  BlossomHistoryDTO<E> historyDTO = buildDTObase(RevisionType.MOD, revision, beforeEntity, afterEntity);

    if (!afterEntity.getId().equals(beforeEntity.getId())) {
      throw new BlossomHistoryIncoherentDataException("Both entities should have the same id");
    }

    if (!afterEntity.getClass().equals(beforeEntity.getClass())) {
      throw new BlossomHistoryIncoherentDataException("Both entities should be of the same type");
    }

    return historyDTO;
  }

  public <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTOfromMOD(BlossomRevisionEntity revision, E afterEntity)
      throws BlossomHistoryIncompleteDataException, BlossomHistoryTechnicalException {
    return buildDTObase(RevisionType.MOD, revision, null, afterEntity);
  }

  public <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTOfromDEL(BlossomRevisionEntity revision, E beforeEntity)
      throws BlossomHistoryIncompleteDataException, BlossomHistoryTechnicalException {
    return buildDTObase(RevisionType.DEL, revision, beforeEntity, null);
  }

  private <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> buildDTObase(RevisionType revisionType,
      BlossomRevisionEntity revision, E beforeEntity, E afterEntity) throws BlossomHistoryIncompleteDataException,
      BlossomHistoryTechnicalException {

    if (beforeEntity != null && beforeEntity.getId() == null || afterEntity != null && afterEntity.getId() == null) {
      throw new BlossomHistoryIncompleteDataException("Entity should have an id");
    }

    return doBuildDTOBase(revisionType, revision, beforeEntity, afterEntity);
  }

  protected abstract <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> doBuildDTOBase(RevisionType revisionType,
      BlossomRevisionEntity revision, E beforeEntity, E afterEntity) throws BlossomHistoryTechnicalException;
}
