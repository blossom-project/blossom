package fr.mgargadennec.blossom.core.user.dao;

import org.springframework.security.access.annotation.Secured;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.core.user.constants.BlossomUserConst;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;
import fr.mgargadennec.blossom.security.core.dao.IBlossomSecuredGenericDao;

@BlossomResourceType(BlossomUserConst.BLOSSOM_USER_ENTITY_NAME)
public interface IBlossomUserDAO extends IBlossomSecuredGenericDao<BlossomUserPO> {

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_READ)
  BlossomUserPO getByEmail(String email);

  @Secured(BlossomConst.SECURITY_GENERIC_DAO_READ)
  BlossomUserPO getByLogin(String login);
}
