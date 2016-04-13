package fr.mgargadennec.blossom.core.association.user.role.process.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import fr.mgargadennec.blossom.core.association.user.role.dao.IBlossomAssociationUserRoleDAO;
import fr.mgargadennec.blossom.core.association.user.role.model.BlossomAssociationUserRolePO;
import fr.mgargadennec.blossom.core.association.user.role.process.IBlossomAssociationUserRoleProcess;
import fr.mgargadennec.blossom.core.association.user.role.process.dto.BlossomAssociationUserRoleProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.impl.BlossomGenericProcessImpl;

public class BlossomAssociationUserRoleProcessImpl extends
    BlossomGenericProcessImpl<BlossomAssociationUserRolePO, BlossomAssociationUserRoleProcessDTO> implements
    IBlossomAssociationUserRoleProcess {

  IBlossomAssociationUserRoleDAO boAssociationUserRoleDAO;

  public BlossomAssociationUserRoleProcessImpl(IBlossomAssociationUserRoleDAO boAssociationUserRoleDAO) {
    super(boAssociationUserRoleDAO);
    this.boAssociationUserRoleDAO = boAssociationUserRoleDAO;
  }

  public Page<BlossomAssociationUserRoleProcessDTO> getAllByUserId(Pageable pageable, Long userId) {
    Page<BlossomAssociationUserRolePO> boPOPage = boAssociationUserRoleDAO.getAllByUserId(pageable, userId);
    return pageProcessDTOFromPagePO(boPOPage, pageable);
  }

  public Page<BlossomAssociationUserRoleProcessDTO> getAllByRoleId(Pageable pageable, Long roleId) {
    Page<BlossomAssociationUserRolePO> boAssociationPOPage = boAssociationUserRoleDAO.getAllByRoleId(pageable, roleId);
    return pageProcessDTOFromPagePO(boAssociationPOPage, pageable);
  }

  public Page<BlossomAssociationUserRoleProcessDTO> getAllByUserIdAndRoleId(Pageable pageable, Long userId, Long roleId) {
    Page<BlossomAssociationUserRolePO> boAssociationPOPage = boAssociationUserRoleDAO.getAllByUserIdAndRoleId(pageable,
        userId, roleId);
    return pageProcessDTOFromPagePO(boAssociationPOPage, pageable);
  }

  public void deleteByUserId(Long userId) {
    boAssociationUserRoleDAO.deleteByUserId(userId);
  }

  public void deleteByRoleId(Long roleId) {
    boAssociationUserRoleDAO.deleteByRoleId(roleId);
  }

  public BlossomAssociationUserRoleProcessDTO createProcessDTOfromPO(BlossomAssociationUserRolePO po) {
    if (po == null) {
      return null;
    }

    BlossomAssociationUserRoleProcessDTO processDTO = new BlossomAssociationUserRoleProcessDTO();
    fillProcessDTOFromPO(processDTO, po);
    return processDTO;

  }

  protected void fillProcessDTOFromPO(BlossomAssociationUserRoleProcessDTO processDTO, BlossomAssociationUserRolePO po) {
    super.fillProcessDTOFromPO(processDTO, po);
    processDTO.setUserId(po.getUserId());
    processDTO.setRoleId(po.getRoleId());
  }

  public BlossomAssociationUserRolePO createPOfromProcessDTO(BlossomAssociationUserRoleProcessDTO processDTO) {
    if (processDTO == null) {
      return null;
    }

    BlossomAssociationUserRolePO po = new BlossomAssociationUserRolePO();
    fillPOFromProcessDTO(po, processDTO);

    return po;
  }

  protected void fillPOFromProcessDTO(BlossomAssociationUserRolePO po, BlossomAssociationUserRoleProcessDTO processDTO) {
    super.fillPOFromProcessDTO(po, processDTO);
    po.setUserId(processDTO.getUserId());
    po.setRoleId(processDTO.getRoleId());
  }
}
