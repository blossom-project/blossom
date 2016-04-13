package fr.mgargadennec.blossom.core.common.process.generic.impl;

import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.common.dao.generic.IBlossomGenericDao;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.process.BlossomAbstractProcessDTO;
import fr.mgargadennec.blossom.core.common.process.generic.IBlossomGenericProcess;

public abstract class BlossomGenericProcessImpl<I extends BlossomAbstractEntity, O extends BlossomAbstractProcessDTO> extends
BlossomGenericReadProcessImpl<I, O> implements IBlossomGenericProcess<I, O> {

  IBlossomGenericDao<I> BlossomGenericDao;

  public BlossomGenericProcessImpl(IBlossomGenericDao<I> genericDAO) {
    super(genericDAO);
    this.BlossomGenericDao = genericDAO;
  }

  @Transactional
  public O create(O boProcessDTO) {
    I poToCreate = createPOfromProcessDTO(boProcessDTO);
    I createdPO = BlossomGenericDao.create(poToCreate);
    return createProcessDTOfromPO(createdPO);
  }

  @Transactional
  public O update(O boProcessDTO) {
    I poToUpdate = createPOfromProcessDTO(boProcessDTO);
    I updatedPO = BlossomGenericDao.update(poToUpdate);
    return createProcessDTOfromPO(updatedPO);
  }

  @Transactional
  public void delete(Long id) {
    BlossomGenericDao.delete(id);
  }

  protected void fillProcessDTOFromPO(O processDTO, I po) {
    fillGenericProcessDTOFromGenericPO(processDTO, po);
  }

  protected void fillPOFromProcessDTO(I po, O processDTO) {
    fillGenericPOFromGenericProcessDTO(po, processDTO);
  }

  protected static final void fillGenericProcessDTOFromGenericPO(BlossomAbstractProcessDTO processDTO, BlossomAbstractEntity po) {
    processDTO.setId(po.getId());
    processDTO.setDateCreation(po.getDateCreation());
    processDTO.setDateModification(po.getDateModification());
    processDTO.setUserCreation(po.getUserCreation());
    processDTO.setUserModification(po.getUserModification());
  }

  protected static final void fillGenericPOFromGenericProcessDTO(BlossomAbstractEntity po, BlossomAbstractProcessDTO processDTO) {
    po.setDateCreation(processDTO.getDateCreation());
    po.setDateModification(processDTO.getDateModification());
    po.setId(processDTO.getId());
    po.setUserCreation(processDTO.getUserCreation());
    po.setUserModification(processDTO.getUserModification());
  }
}
