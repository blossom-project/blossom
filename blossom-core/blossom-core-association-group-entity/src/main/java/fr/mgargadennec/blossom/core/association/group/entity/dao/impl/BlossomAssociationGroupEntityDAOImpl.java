package fr.mgargadennec.blossom.core.association.group.entity.dao.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.group.entity.dao.IBlossomAssociationGroupEntityDAO;
import fr.mgargadennec.blossom.core.association.group.entity.model.BlossomAssociationGroupEntityPO;
import fr.mgargadennec.blossom.core.association.group.entity.repository.BlossomAssociationGroupEntityRepository;
import fr.mgargadennec.blossom.core.common.dao.generic.impl.BlossomGenericDAOImpl;

public class BlossomAssociationGroupEntityDAOImpl extends BlossomGenericDAOImpl<BlossomAssociationGroupEntityPO> implements
    IBlossomAssociationGroupEntityDAO {

  private BlossomAssociationGroupEntityRepository boGroupEntityRepository;

  public BlossomAssociationGroupEntityDAOImpl(BlossomAssociationGroupEntityRepository boGroupEntityRepository) {
    super(boGroupEntityRepository);
    this.boGroupEntityRepository = boGroupEntityRepository;
  }

  public Page<BlossomAssociationGroupEntityPO> getAllByEntityIdAndEntityType(Pageable pageable, Long entityId,
      String resourceType) {
    return boGroupEntityRepository.findAllByEntityIdAndEntityType(pageable, entityId, resourceType);
  }

  public Page<BlossomAssociationGroupEntityPO> getAllByEntityType(Pageable pageable, String resourceType) {
    return boGroupEntityRepository.findAllByEntityType(pageable, resourceType);
  }

  public Page<BlossomAssociationGroupEntityPO> getAllByEntityTypeAndGroupId(Pageable pageable, String resourceType,
      Long groupId) {
    return boGroupEntityRepository.findAllByEntityTypeAndGroupId(pageable, resourceType, groupId);

  }

  public Page<BlossomAssociationGroupEntityPO> getAllByEntityIdAndEntityTypeandGroupId(Pageable pageable, Long entityId,
      String resourceType, Long groupId) {
    return boGroupEntityRepository.findAllByEntityIdAndEntityTypeAndGroupId(pageable, entityId, resourceType, groupId);
  }

  public void deleteByGroupIdAndEntityIdAndEntityType(Long groupId, Long entityId, String resourceType) {
    boGroupEntityRepository.deleteByGroupIdAndEntityIdAndEntityType(groupId, entityId, resourceType);
  }

  public void deleteByEntityIdAndEntityType(Long entityId, String resourceType) {
    boGroupEntityRepository.deleteByEntityIdAndEntityType(entityId, resourceType);
  }

  public void deleteByGroupId(Long groupId) {
    boGroupEntityRepository.deleteByGroupId(groupId);
  }

}
