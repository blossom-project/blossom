package fr.mgargadennec.blossom.core.user.dao.impl;

import fr.mgargadennec.blossom.core.user.dao.IBlossomUserDAO;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;
import fr.mgargadennec.blossom.core.user.repository.BlossomUserRepository;
import fr.mgargadennec.blossom.security.core.dao.impl.BlossomSecuredGenericDAOImpl;

public class BlossomUserDAOImpl extends BlossomSecuredGenericDAOImpl<BlossomUserPO> implements IBlossomUserDAO {

  private BlossomUserRepository boUserRepository;

  public BlossomUserDAOImpl(BlossomUserRepository boUserRepository) {
    super(boUserRepository);
    this.boUserRepository = boUserRepository;
  }

  public BlossomUserPO getByEmail(String email) {
    return boUserRepository.findByEmail(email);
  }

  public BlossomUserPO getByLogin(String login) {
    return boUserRepository.findByLogin(login);
  }

}
