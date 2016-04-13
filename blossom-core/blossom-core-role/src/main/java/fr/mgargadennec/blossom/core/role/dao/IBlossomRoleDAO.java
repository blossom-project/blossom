package fr.mgargadennec.blossom.core.role.dao;

import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.core.role.constants.BlossomRoleConst;
import fr.mgargadennec.blossom.core.role.model.BlossomRolePO;
import fr.mgargadennec.blossom.security.core.dao.IBlossomSecuredGenericDao;

@BlossomResourceType(BlossomRoleConst.BLOSSOM_ROLE_ENTITY_NAME)
public interface IBlossomRoleDAO extends IBlossomSecuredGenericDao<BlossomRolePO> {

}
