package fr.mgargadennec.blossom.core.role.process.impl;

import fr.mgargadennec.blossom.core.common.process.generic.impl.BlossomGenericProcessImpl;
import fr.mgargadennec.blossom.core.role.dao.IBlossomRoleDAO;
import fr.mgargadennec.blossom.core.role.model.BlossomRolePO;
import fr.mgargadennec.blossom.core.role.process.IBlossomRoleProcess;
import fr.mgargadennec.blossom.core.role.process.dto.BlossomRoleProcessDTO;

public class BlossomRoleProcessImpl extends BlossomGenericProcessImpl<BlossomRolePO, BlossomRoleProcessDTO> implements IBlossomRoleProcess {

  public BlossomRoleProcessImpl(IBlossomRoleDAO boRoleDAO) {
    super(boRoleDAO);
  }

  public BlossomRoleProcessDTO createProcessDTOfromPO(BlossomRolePO po) {
    if (po == null) {
      return null;
    }

    BlossomRoleProcessDTO processDTO = new BlossomRoleProcessDTO();
    fillProcessDTOFromPO(processDTO, po);
    return processDTO;
  }

  public BlossomRolePO createPOfromProcessDTO(BlossomRoleProcessDTO processDTO) {
    if (processDTO == null) {
      return null;
    }

    BlossomRolePO po = new BlossomRolePO();
    fillPOFromProcessDTO(po, processDTO);
    return po;
  }
  
  @Override
  protected void fillProcessDTOFromPO(BlossomRoleProcessDTO processDTO, BlossomRolePO po) {
    super.fillProcessDTOFromPO(processDTO, po);
    processDTO.setName(po.getName());
    processDTO.setDescription(po.getDescription());
  }


  @Override
  protected void fillPOFromProcessDTO(BlossomRolePO po, BlossomRoleProcessDTO processDTO) {
    super.fillPOFromProcessDTO(po, processDTO);
    po.setName(processDTO.getName());
    po.setDescription(processDTO.getDescription());
  }
}
