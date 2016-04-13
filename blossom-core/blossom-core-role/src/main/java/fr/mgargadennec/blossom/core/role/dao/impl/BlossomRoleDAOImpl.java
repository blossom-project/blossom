package fr.mgargadennec.blossom.core.role.dao.impl;

import fr.mgargadennec.blossom.core.role.dao.IBlossomRoleDAO;
import fr.mgargadennec.blossom.core.role.model.BlossomRolePO;
import fr.mgargadennec.blossom.core.role.repository.BlossomRoleRepository;
import fr.mgargadennec.blossom.security.core.dao.impl.BlossomSecuredGenericDAOImpl;

public class BlossomRoleDAOImpl extends BlossomSecuredGenericDAOImpl<BlossomRolePO> implements IBlossomRoleDAO {

  public BlossomRoleDAOImpl(BlossomRoleRepository boRoleRepository) {
    super(boRoleRepository);
  }

}
