package fr.mgargadennec.blossom.core.right.dao.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.common.dao.generic.impl.BlossomGenericDAOImpl;
import fr.mgargadennec.blossom.core.right.dao.IBlossomRightDAO;
import fr.mgargadennec.blossom.core.right.model.BlossomRightPO;
import fr.mgargadennec.blossom.core.right.repository.BlossomRightRepository;

public class BlossomRightDAOImpl extends BlossomGenericDAOImpl<BlossomRightPO> implements IBlossomRightDAO {

  private BlossomRightRepository boRightRepository;

  public BlossomRightDAOImpl(BlossomRightRepository boRightRepository) {
    super(boRightRepository);
    this.boRightRepository = boRightRepository;
  }

  public Page<BlossomRightPO> getByRoleId(Pageable pageable, Long roleId) {
    return boRightRepository.findByRoleId(pageable, roleId);
  }

  @Transactional
  public void deleteByRoleId(Long roleId) {
    boRightRepository.deleteByRoleId(roleId);
  }
}
