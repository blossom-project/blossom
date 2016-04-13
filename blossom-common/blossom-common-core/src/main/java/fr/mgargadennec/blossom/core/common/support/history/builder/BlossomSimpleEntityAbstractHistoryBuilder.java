package fr.mgargadennec.blossom.core.common.support.history.builder;

import javax.persistence.Entity;

import org.hibernate.envers.RevisionType;

import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryDTO;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryTechnicalException;

public abstract class BlossomSimpleEntityAbstractHistoryBuilder extends BlossomAbstractHistoryBuilder {

	public BlossomSimpleEntityAbstractHistoryBuilder(IBlossomEntityDiffBuilder diffBuilder) {
		super(diffBuilder);
	}

	@Override
	protected <E extends BlossomAbstractEntity> BlossomHistoryDTO<E> doBuildDTOBase(RevisionType revisionType,
			BlossomRevisionEntity revision, E beforeEntity, E afterEntity) throws BlossomHistoryTechnicalException {

		BlossomHistoryDTO<E> historyDTO = new BlossomHistoryDTO<E>();
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

		historyDTO.setEntityId(entityToUse.getId());
		historyDTO.setEntityClass(entityToUse.getClass().getCanonicalName());
		historyDTO.setEntityName(entityToUse.getClass().getAnnotation(Entity.class).name());
		historyDTO.setUserCreation(entityToUse.getUserCreation());

		if (beforeEntity == null) {
			historyDTO.setDiff(diffBuilder.computeDiffWithoutBefore(afterEntity));
		} else if (afterEntity == null) {
			historyDTO.setDiff(diffBuilder.computeDiffWithoutAfter(beforeEntity));
		} else {
			historyDTO.setDiff(diffBuilder.computeDiff(beforeEntity, afterEntity));
		}

		return historyDTO;
	}
}
