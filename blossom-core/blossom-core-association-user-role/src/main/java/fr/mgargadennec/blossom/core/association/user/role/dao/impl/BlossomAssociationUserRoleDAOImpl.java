package fr.mgargadennec.blossom.core.association.user.role.dao.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.association.user.role.dao.IBlossomAssociationUserRoleDAO;
import fr.mgargadennec.blossom.core.association.user.role.model.BlossomAssociationUserRolePO;
import fr.mgargadennec.blossom.core.association.user.role.repository.BlossomAssociationUserRoleRepository;
import fr.mgargadennec.blossom.security.core.dao.impl.BlossomSecuredGenericDAOImpl;

public class BlossomAssociationUserRoleDAOImpl extends BlossomSecuredGenericDAOImpl<BlossomAssociationUserRolePO> implements
    IBlossomAssociationUserRoleDAO {

  private BlossomAssociationUserRoleRepository boUserRoleRepository;

  public BlossomAssociationUserRoleDAOImpl(BlossomAssociationUserRoleRepository boUserRoleRepository) {
    super(boUserRoleRepository);
    this.boUserRoleRepository = boUserRoleRepository;
  }

  public Page<BlossomAssociationUserRolePO> getAllByRoleId(Pageable pageable, Long roleId) {
    return boUserRoleRepository.findAllByRoleId(pageable, roleId);
  }

  public Page<BlossomAssociationUserRolePO> getAllByUserId(Pageable pageable, Long userId) {
    return boUserRoleRepository.findAllByUserId(pageable, userId);
  }

  public Page<BlossomAssociationUserRolePO> getAllByUserIdAndRoleId(Pageable pageable, Long userId, Long roleId) {
    return boUserRoleRepository.findAllByUserIdAndRoleId(pageable, userId, roleId);
  }

  @Transactional
  public void deleteByUserId(Long userId) {
    boUserRoleRepository.deleteByUserId(userId);
  }

  @Transactional
  public void deleteByRoleId(Long roleId) {
    boUserRoleRepository.deleteByRoleId(roleId);
  }
}
