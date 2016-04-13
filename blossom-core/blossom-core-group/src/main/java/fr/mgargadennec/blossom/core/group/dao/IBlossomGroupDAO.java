package fr.mgargadennec.blossom.core.group.dao;

import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.core.group.constants.BlossomGroupConst;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;
import fr.mgargadennec.blossom.security.core.dao.IBlossomSecuredGenericDao;

@BlossomResourceType(BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME)
public interface IBlossomGroupDAO extends IBlossomSecuredGenericDao<BlossomGroupPO> {

}
