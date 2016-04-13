package fr.mgargadennec.blossom.core.association.group.user.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import fr.mgargadennec.blossom.core.association.group.user.constants.BlossomAssociationGroupUserConst;
import fr.mgargadennec.blossom.core.association.group.user.model.BlossomAssociationGroupUserPO;
import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.security.core.dao.IBlossomSecuredGenericDao;

@BlossomResourceType(BlossomAssociationGroupUserConst.BLOSSOM_ASSOCIATION_GROUP_USER_ENTITY_NAME)
public interface IBlossomAssociationGroupUserDAO extends IBlossomSecuredGenericDao<BlossomAssociationGroupUserPO> {

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_READ)
  Page<BlossomAssociationGroupUserPO> getAllByUserId(Pageable pageable, Long userId);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_READ)
  Page<BlossomAssociationGroupUserPO> getAllByGroupId(Pageable pageable, Long groupId);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_READ)
  Page<BlossomAssociationGroupUserPO> getAllByGroupIdAndUserId(Pageable pageable, Long groupId, Long userId);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_DELETE)
  void deleteByGroupId(Long groupId);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_DELETE)
  void deleteByUserId(Long userId);

}
