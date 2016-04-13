package fr.mgargadennec.blossom.core.association.user.role.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.user.role.process.dto.BlossomAssociationUserRoleProcessDTO;
import fr.mgargadennec.blossom.core.association.user.role.service.dto.BlossomAssociationUserRoleServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericService;

public interface IBlossomAssociationUserRoleService extends
    IBlossomGenericService<BlossomAssociationUserRoleProcessDTO, BlossomAssociationUserRoleServiceDTO> {

  Page<BlossomAssociationUserRoleServiceDTO> getAll(Pageable pageable, Long roleId, Long userId);

  void deleteByUserId(Long userId);

  void deleteByRoleId(Long roleId);

}
