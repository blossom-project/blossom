/**
 *
 */
package fr.mgargadennec.blossom.core.common.support.history.builder;

import javax.persistence.Entity;

import org.hibernate.envers.RevisionType;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryAssociationDTO;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryDTO;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryTechnicalException;

public abstract class BlossomAssociationEntityAbstractHistoryBuilder<M extends BlossomAbstractEntity, S extends BlossomAbstractEntity>
		extends BlossomAbstractHistoryBuilder {

	public BlossomAssociationEntityAbstractHistoryBuilder(IBlossomEntityDiffBuilder diffBuilder) {
		super(diffBuilder);
	}

	@Override
	protected <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> doBuildDTOBase(RevisionType revisionType,
			BlossomRevisionEntity revision, E beforeEntity, E afterEntity) throws BlossomHistoryTechnicalException {

		BlossomHistoryAssociationDTO<E, M, S> historyDTO = new BlossomHistoryAssociationDTO<E, M, S>();
		historyDTO.setRevisionType(revisionType);
		historyDTO.setRevisionId(revision.getId());
		historyDTO.setRevisionTimestamp(revision.getTimestamp());
		historyDTO.setUserModification(revision.getUser());
		historyDTO.setBeforeEntity(beforeEntity);
		historyDTO.setAfterEntity(afterEntity);

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

		if (beforeEntity == null) {
			historyDTO.setDiff(diffBuilder.computeDiffWithoutBefore(afterEntity));
		} else if (afterEntity == null) {
			historyDTO.setDiff(diffBuilder.computeDiffWithoutAfter(beforeEntity));
		} else {
			historyDTO.setDiff(diffBuilder.computeDiff(beforeEntity, afterEntity));
		}

		M masterEntity = getMasterEntity(revision, entityToUse);
		historyDTO.setMasterClass(getMasterEntityClass(revision, entityToUse).getCanonicalName());
		historyDTO.setMasterName(getMasterEntityClass(revision, entityToUse).getAnnotation(Entity.class).name());
		historyDTO.setMasterEntity(masterEntity);
		historyDTO.setMasterEntityId(masterEntity != null ? masterEntity.getId().toString() : null);

		S slaveEntity = getSlaveEntity(revision, entityToUse);
		historyDTO.setSlaveClass(getSlaveEntityClass(revision, entityToUse).getCanonicalName());
		historyDTO.setSlaveName(getSlaveEntityClass(revision, entityToUse).getAnnotation(Entity.class).name());
		historyDTO.setSlaveEntity(getSlaveEntity(revision, entityToUse));
		historyDTO.setSlaveEntityId(slaveEntity != null ? slaveEntity.getId().toString() : null);

		return historyDTO;
	}

	protected abstract <E extends BlossomAbstractEntity> Class<M> getMasterEntityClass(BlossomRevisionEntity revision,
			E entity);

	protected abstract <E extends BlossomAbstractEntity> M getMasterEntity(BlossomRevisionEntity revision, E entity);

	protected abstract <E extends BlossomAbstractEntity> Class<S> getSlaveEntityClass(BlossomRevisionEntity revision,
			E entity);

	protected abstract <E extends BlossomAbstractEntity> S getSlaveEntity(BlossomRevisionEntity revision, E entity);

}
