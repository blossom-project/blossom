package fr.mgargadennec.blossom.core.association.user.role.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import fr.mgargadennec.blossom.core.association.user.role.constants.BlossomAssociationUserRoleConst;
import fr.mgargadennec.blossom.core.association.user.role.model.BlossomAssociationUserRolePO;
import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.security.core.dao.IBlossomSecuredGenericDao;

@BlossomResourceType(BlossomAssociationUserRoleConst.BLOSSOM_ASSOCIATION_USER_ROLE_ENTITY_NAME)
public interface IBlossomAssociationUserRoleDAO extends IBlossomSecuredGenericDao<BlossomAssociationUserRolePO> {

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_READ)
  Page<BlossomAssociationUserRolePO> getAllByRoleId(Pageable pageable, Long roleId);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_READ)
  Page<BlossomAssociationUserRolePO> getAllByUserId(Pageable pageable, Long userId);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_READ)
  Page<BlossomAssociationUserRolePO> getAllByUserIdAndRoleId(Pageable pageable, Long userId, Long roleId);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_DELETE)
  void deleteByUserId(Long userId);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_DELETE)
  void deleteByRoleId(Long rightId);

}
