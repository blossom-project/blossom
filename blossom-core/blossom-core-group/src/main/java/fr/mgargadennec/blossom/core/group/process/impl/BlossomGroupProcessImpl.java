package fr.mgargadennec.blossom.core.group.process.impl;

import fr.mgargadennec.blossom.core.common.process.generic.impl.BlossomGenericProcessImpl;
import fr.mgargadennec.blossom.core.group.dao.IBlossomGroupDAO;
import fr.mgargadennec.blossom.core.group.model.BlossomGroupPO;
import fr.mgargadennec.blossom.core.group.process.IBlossomGroupProcess;
import fr.mgargadennec.blossom.core.group.process.dto.BlossomGroupProcessDTO;

public class BlossomGroupProcessImpl extends BlossomGenericProcessImpl<BlossomGroupPO, BlossomGroupProcessDTO> implements IBlossomGroupProcess {

  public BlossomGroupProcessImpl(IBlossomGroupDAO boGroupDAO) {
    super(boGroupDAO);
  }

  public BlossomGroupProcessDTO createProcessDTOfromPO(BlossomGroupPO po) {

    if (po == null) {
      return null;
    }

    BlossomGroupProcessDTO processDTO = new BlossomGroupProcessDTO();
    fillProcessDTOFromPO(processDTO, po);
    return processDTO;
  }
  

  public BlossomGroupPO createPOfromProcessDTO(BlossomGroupProcessDTO processDTO) {
    if (processDTO == null) {
      return null;
    }

    BlossomGroupPO po = new BlossomGroupPO();
    fillPOFromProcessDTO(po, processDTO);

    return po;
  }


  @Override
  protected void fillProcessDTOFromPO(BlossomGroupProcessDTO processDTO, BlossomGroupPO po) {
    super.fillProcessDTOFromPO(processDTO, po);
    processDTO.setDescription(po.getDescription());
    processDTO.setName(po.getName());
  }

  @Override
  protected void fillPOFromProcessDTO(BlossomGroupPO po, BlossomGroupProcessDTO processDTO) {
    super.fillPOFromProcessDTO(po, processDTO);
    po.setDescription(processDTO.getDescription());
    po.setName(processDTO.getName());
  }

}
