package fr.mgargadennec.blossom.core.association.group.entity.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.group.entity.model.BlossomAssociationGroupEntityPO;
import fr.mgargadennec.blossom.core.common.dao.generic.IBlossomGenericDao;

public interface IBlossomAssociationGroupEntityDAO extends IBlossomGenericDao<BlossomAssociationGroupEntityPO> {

	Page<BlossomAssociationGroupEntityPO> getAllByEntityIdAndEntityType(Pageable pageable, Long entityId,
			String entityType);

	Page<BlossomAssociationGroupEntityPO> getAllByEntityType(Pageable pageable, String string);

	Page<BlossomAssociationGroupEntityPO> getAllByEntityTypeAndGroupId(Pageable pageable, String resourceType,
			Long groupId);

	Page<BlossomAssociationGroupEntityPO> getAllByEntityIdAndEntityTypeandGroupId(Pageable pageable, Long entityId,
			String resourceType, Long groupId);

	void deleteByGroupIdAndEntityIdAndEntityType(Long groupId, Long entityId, String entityType);

	void deleteByEntityIdAndEntityType(Long entityId, String entityType);

	void deleteByGroupId(Long groupId);

}
