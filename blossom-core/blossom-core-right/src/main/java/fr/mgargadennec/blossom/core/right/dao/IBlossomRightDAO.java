package fr.mgargadennec.blossom.core.right.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.annotation.Secured;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.core.right.constants.BlossomRightConst;
import fr.mgargadennec.blossom.core.right.model.BlossomRightPO;
import fr.mgargadennec.blossom.security.core.dao.IBlossomSecuredGenericDao;

@BlossomResourceType(BlossomRightConst.BLOSSOM_RIGHT_ENTITY_NAME)
public interface IBlossomRightDAO extends IBlossomSecuredGenericDao<BlossomRightPO> {

  @Secured({BlossomConst.SECURITY_GENERIC_DAO_READ})
  Page<BlossomRightPO> getByRoleId(Pageable pageable, Long roleId);

  @Secured({BlossomConst.SECURITY_GENERIC_DAO_DELETE})
  void deleteByRoleId(Long roleId);

}
