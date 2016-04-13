package fr.mgargadennec.blossom.core.association.group.user.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.group.user.process.dto.BlossomAssociationGroupUserProcessDTO;
import fr.mgargadennec.blossom.core.association.group.user.service.dto.BlossomAssociationGroupUserServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericService;

public interface IBlossomAssociationGroupUserService
		extends IBlossomGenericService<BlossomAssociationGroupUserProcessDTO, BlossomAssociationGroupUserServiceDTO> {

	Page<BlossomAssociationGroupUserServiceDTO> getAll(Pageable pageable, Long userId, Long groupId);

	void deleteByGroupId(Long groupId);

	void deleteByUserId(Long userId);

}
