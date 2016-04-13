package fr.mgargadennec.blossom.core.association.user.role.process;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.user.role.model.BlossomAssociationUserRolePO;
import fr.mgargadennec.blossom.core.association.user.role.process.dto.BlossomAssociationUserRoleProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericProcess;

public interface IBlossomAssociationUserRoleProcess extends
IBlossomGenericProcess<BlossomAssociationUserRolePO, BlossomAssociationUserRoleProcessDTO> {

  Page<BlossomAssociationUserRoleProcessDTO> getAllByUserId(Pageable pageable, Long userId);

  Page<BlossomAssociationUserRoleProcessDTO> getAllByRoleId(Pageable pageable, Long roleId);

  Page<BlossomAssociationUserRoleProcessDTO> getAllByUserIdAndRoleId(Pageable pageable, Long userId, Long rightId);

  BlossomAssociationUserRoleProcessDTO get(Long associationUserRightId);

  BlossomAssociationUserRoleProcessDTO create(BlossomAssociationUserRoleProcessDTO boAssociationUserRightProcessDTO);

  BlossomAssociationUserRoleProcessDTO update(BlossomAssociationUserRoleProcessDTO boAssociationUserRightProcessDTO);

  void delete(Long associationUserRightId);

  void deleteByUserId(Long userId);

  void deleteByRoleId(Long rightId);

}
