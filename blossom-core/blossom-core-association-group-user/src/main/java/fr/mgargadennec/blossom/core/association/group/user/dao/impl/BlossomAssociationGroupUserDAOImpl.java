package fr.mgargadennec.blossom.core.association.group.user.dao.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.association.group.user.dao.IBlossomAssociationGroupUserDAO;
import fr.mgargadennec.blossom.core.association.group.user.model.BlossomAssociationGroupUserPO;
import fr.mgargadennec.blossom.core.association.group.user.repository.BlossomAssociationGroupUserRepository;
import fr.mgargadennec.blossom.security.core.dao.impl.BlossomSecuredGenericDAOImpl;

public class BlossomAssociationGroupUserDAOImpl extends BlossomSecuredGenericDAOImpl<BlossomAssociationGroupUserPO> implements
    IBlossomAssociationGroupUserDAO {

  private BlossomAssociationGroupUserRepository boGroupUserRepository;

  public BlossomAssociationGroupUserDAOImpl(BlossomAssociationGroupUserRepository boGroupUserRepository) {
    super(boGroupUserRepository);
    this.boGroupUserRepository = boGroupUserRepository;
  }

  public Page<BlossomAssociationGroupUserPO> getAllByUserId(Pageable pageable, Long userId) {
    return boGroupUserRepository.findAllByUserId(pageable, userId);
  }

  public Page<BlossomAssociationGroupUserPO> getAllByGroupId(Pageable pageable, Long groupId) {
    return boGroupUserRepository.findAllByGroupId(pageable, groupId);
  }

  public Page<BlossomAssociationGroupUserPO> getAllByGroupIdAndUserId(Pageable pageable, Long groupId, Long userId) {
    return boGroupUserRepository.findAllByGroupIdAndUserId(pageable, groupId, userId);
  }

  @Transactional
  public void deleteByGroupId(Long groupId) {
    boGroupUserRepository.deleteByGroupId(groupId);
  }

  @Transactional
  public void deleteByUserId(Long userId) {
    boGroupUserRepository.deleteByUserId(userId);
  }
}
