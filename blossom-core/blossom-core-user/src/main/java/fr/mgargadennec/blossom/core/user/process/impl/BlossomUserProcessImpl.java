package fr.mgargadennec.blossom.core.user.process.impl;

import fr.mgargadennec.blossom.core.common.process.generic.impl.BlossomGenericProcessImpl;
import fr.mgargadennec.blossom.core.user.dao.IBlossomUserDAO;
import fr.mgargadennec.blossom.core.user.model.BlossomUserPO;
import fr.mgargadennec.blossom.core.user.process.IBlossomUserProcess;
import fr.mgargadennec.blossom.core.user.process.dto.BlossomUserProcessDTO;

public class BlossomUserProcessImpl extends BlossomGenericProcessImpl<BlossomUserPO, BlossomUserProcessDTO> implements IBlossomUserProcess {

  IBlossomUserDAO boUserDAO;

  public BlossomUserProcessImpl(IBlossomUserDAO boUserDAO) {
    super(boUserDAO);
    this.boUserDAO = boUserDAO;
  }

  public BlossomUserProcessDTO getByEmail(String email) {
    return createProcessDTOfromPO(boUserDAO.getByEmail(email));
  }

  public BlossomUserProcessDTO getByLogin(String login) {
    return createProcessDTOfromPO(boUserDAO.getByLogin(login));
  }

  public BlossomUserProcessDTO createProcessDTOfromPO(BlossomUserPO po) {

    if (po == null) {
      return null;
    }

    BlossomUserProcessDTO processDTO = new BlossomUserProcessDTO();
    fillProcessDTOFromPO(processDTO, po);

    return processDTO;
  }

  @Override
  protected void fillProcessDTOFromPO(BlossomUserProcessDTO processDTO, BlossomUserPO po) {
    super.fillProcessDTOFromPO(processDTO, po);
    processDTO.setFirstname(po.getFirstname());
    processDTO.setLastname(po.getLastname());
    processDTO.setEmail(po.getEmail());
    processDTO.setPhone(po.getPhone());
    processDTO.setLogin(po.getLogin());
    processDTO.setPassword(po.getPassword());
    processDTO.setState(po.getState());
    processDTO.setFunction(po.getFunction());
    processDTO.setRoot(po.isRoot());
  }

  public BlossomUserPO createPOfromProcessDTO(BlossomUserProcessDTO processDTO) {
    if (processDTO == null) {
      return null;
    }

    BlossomUserPO po = new BlossomUserPO();
    fillPOFromProcessDTO(po, processDTO);
    return po;
  }

  @Override
  protected void fillPOFromProcessDTO(BlossomUserPO po, BlossomUserProcessDTO processDTO) {
    super.fillPOFromProcessDTO(po, processDTO);
    po.setFirstname(processDTO.getFirstname());
    po.setLastname(processDTO.getLastname());
    po.setEmail(processDTO.getEmail());
    po.setPhone(processDTO.getPhone());
    po.setLogin(processDTO.getLogin());
    po.setPassword(processDTO.getPassword());
    po.setState(processDTO.getState());
    po.setFunction(processDTO.getFunction());
    po.setRoot(processDTO.getRoot());
  }
}
