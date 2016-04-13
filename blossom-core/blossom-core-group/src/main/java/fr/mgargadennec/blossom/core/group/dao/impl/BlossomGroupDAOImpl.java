package fr.mgargadennec.blossom.core.group.dao.impl;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.group.dao.IBlossomGroupDAO;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;
import fr.mgargadennec.blossom.core.group.repository.BlossomGroupRepository;
import fr.mgargadennec.blossom.security.core.dao.impl.BlossomSecuredGenericDAOImpl;

public class BlossomGroupDAOImpl extends BlossomSecuredGenericDAOImpl<BlossomGroupPO> implements IBlossomGroupDAO {

  public BlossomGroupDAOImpl(BlossomGroupRepository boGroupRepository) {
    super(boGroupRepository);
  }

  @Override
  public void delete(Long id) {
    if (id != null && id.equals(BlossomConst.SECURITY_GROUP_ROOT_ID)) {
      throw new IllegalArgumentException("Cannot delete root Group");
    }
    super.delete(id);
  }

}
