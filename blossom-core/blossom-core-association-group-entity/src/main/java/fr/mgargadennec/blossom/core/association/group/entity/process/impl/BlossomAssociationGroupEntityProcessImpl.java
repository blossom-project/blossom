package fr.mgargadennec.blossom.core.association.group.entity.process.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.group.entity.dao.IBlossomAssociationGroupEntityDAO;
import fr.mgargadennec.blossom.core.association.group.entity.model.BlossomAssociationGroupEntityPO;
import fr.mgargadennec.blossom.core.association.group.entity.process.IBlossomAssociationGroupEntityProcess;
import fr.mgargadennec.blossom.core.association.group.entity.process.dto.BlossomAssociationGroupEntityProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.impl.BlossomGenericProcessImpl;

public class BlossomAssociationGroupEntityProcessImpl extends
    BlossomGenericProcessImpl<BlossomAssociationGroupEntityPO, BlossomAssociationGroupEntityProcessDTO> implements
    IBlossomAssociationGroupEntityProcess {

  IBlossomAssociationGroupEntityDAO boAssociationGroupEntityDAO;

  public BlossomAssociationGroupEntityProcessImpl(IBlossomAssociationGroupEntityDAO boAssociationGroupEntityDAO) {
    super(boAssociationGroupEntityDAO);
    this.boAssociationGroupEntityDAO = boAssociationGroupEntityDAO;
  }

  public Page<BlossomAssociationGroupEntityProcessDTO> getAllByEntityIdAndEntityType(Pageable pageable, Long entityId,
      String entityType) {
    Page<BlossomAssociationGroupEntityPO> pagePO = boAssociationGroupEntityDAO.getAllByEntityIdAndEntityType(pageable,
        entityId, entityType);

    return pageProcessDTOFromPagePO(pagePO, pageable);
  }

  public Page<BlossomAssociationGroupEntityProcessDTO> getAllByEntityType(Pageable pageable, String entityType) {
    Page<BlossomAssociationGroupEntityPO> pagePO = boAssociationGroupEntityDAO.getAllByEntityType(pageable, entityType);

    return pageProcessDTOFromPagePO(pagePO, pageable);
  }

  public Page<BlossomAssociationGroupEntityProcessDTO> getAllByEntityTypeAndGroupId(Pageable pageable, String resourceType,
      Long groupId) {
    Page<BlossomAssociationGroupEntityPO> pagePO = boAssociationGroupEntityDAO.getAllByEntityTypeAndGroupId(pageable,
        resourceType, groupId);

    return pageProcessDTOFromPagePO(pagePO, pageable);
  }

  public Page<BlossomAssociationGroupEntityProcessDTO> getAllByEntityIdAndEntityTypeAndGroupId(Pageable pageable,
      Long entityId, String entityType, Long groupId) {
    Page<BlossomAssociationGroupEntityPO> pagePO = boAssociationGroupEntityDAO.getAllByEntityIdAndEntityTypeandGroupId(
        pageable, entityId, entityType, groupId);

    return pageProcessDTOFromPagePO(pagePO, pageable);
  }

  public void deleteByGroupIdAndEntityIdAndEntityType(Long groupId, Long entityId, String entityType) {
    boAssociationGroupEntityDAO.deleteByGroupIdAndEntityIdAndEntityType(groupId, entityId, entityType);
  }

  public void deleteByEntityIdAndEntityType(Long entityId, String entityType) {
    boAssociationGroupEntityDAO.deleteByEntityIdAndEntityType(entityId, entityType);
  }

  public void deleteByGroupId(Long groupId) {
    boAssociationGroupEntityDAO.deleteByGroupId(groupId);
  }

  public BlossomAssociationGroupEntityProcessDTO createProcessDTOfromPO(BlossomAssociationGroupEntityPO po) {
    if (po == null) {
      return null;
    }
    BlossomAssociationGroupEntityProcessDTO processDTO = new BlossomAssociationGroupEntityProcessDTO();
    fillProcessDTOFromPO(processDTO, po);
    return processDTO;
  }

  public BlossomAssociationGroupEntityPO createPOfromProcessDTO(BlossomAssociationGroupEntityProcessDTO processDTO) {
    BlossomAssociationGroupEntityPO po = new BlossomAssociationGroupEntityPO();
    fillPOFromProcessDTO(po, processDTO);
    return po;
  }

  protected void fillPOFromProcessDTO(BlossomAssociationGroupEntityPO po, BlossomAssociationGroupEntityProcessDTO processDTO) {
    super.fillPOFromProcessDTO(po, processDTO);
    po.setGroupId(processDTO.getGroupId());
    po.setEntityId(processDTO.getEntityId());
    po.setEntityType(processDTO.getEntityType());
  }

  protected void fillProcessDTOFromPO(BlossomAssociationGroupEntityProcessDTO processDTO, BlossomAssociationGroupEntityPO po) {
    super.fillProcessDTOFromPO(processDTO, po);
    processDTO.setGroupId(po.getGroupId());
    processDTO.setEntityId(po.getEntityId());
    processDTO.setEntityType(po.getEntityType());
  }

}
