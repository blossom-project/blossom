package fr.mgargadennec.blossom.core.association.group.entity.service.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.transaction.annotation.Transactional;

import fr.mgargadennec.blossom.core.association.group.entity.process.IBlossomAssociationGroupEntityProcess;
import fr.mgargadennec.blossom.core.association.group.entity.process.dto.BlossomAssociationGroupEntityProcessDTO;
import fr.mgargadennec.blossom.core.association.group.entity.service.IBlossomAssociationGroupEntityService;
import fr.mgargadennec.blossom.core.association.group.entity.service.dto.BlossomAssociationGroupEntityServiceDTO;
import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;
import fr.mgargadennec.blossom.core.common.service.IBlossomServicePlugin;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityAfterDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityBeforeDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityCreatedEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityUpdatedEvent;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.service.impl.BlossomSecuredGenericServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomAssociationGroupEntityServiceImpl extends
    BlossomSecuredGenericServiceImpl<BlossomAssociationGroupEntityProcessDTO, BlossomAssociationGroupEntityServiceDTO> implements
    IBlossomAssociationGroupEntityService {
  protected Logger logger = LoggerFactory.getLogger(BlossomAssociationGroupEntityServiceImpl.class);
  private IBlossomAssociationGroupEntityProcess boGroupEntityProcess;
  private PluginRegistry<IBlossomServicePlugin, String> servicePluginRegistry;

  public BlossomAssociationGroupEntityServiceImpl(IBlossomAssociationGroupEntityProcess boGroupEntityProcess,
      PluginRegistry<IBlossomServicePlugin, String> servicePluginRegistry,
      IBlossomAuthenticationUtilService boAuthenticationUtilService, ApplicationEventPublisher eventPublisher) {
    super(boGroupEntityProcess, boAuthenticationUtilService, eventPublisher);
    this.boGroupEntityProcess = boGroupEntityProcess;
    this.servicePluginRegistry = servicePluginRegistry;
  }

  @Transactional
  public BlossomAssociationGroupEntityServiceDTO create(BlossomAssociationGroupEntityServiceDTO boAssociationGroupEntityServiceDTO) {

    BlossomAssociationGroupEntityProcessDTO processDTOToCreate = createProcessDTOfromServiceDTO(boAssociationGroupEntityServiceDTO);
    BlossomAssociationGroupEntityProcessDTO createdProcessDTO = boGroupEntityProcess.create(processDTOToCreate);

    final BlossomAssociationGroupEntityServiceDTO result = createServiceDTOfromProcessDTO(createdProcessDTO);

    if (servicePluginRegistry.hasPluginFor(result.getEntityType())) {
      boAuthenticationUtilService.runAs(new Runnable() {
        public void run() {
          try {
            IBlossomServiceDTO serviceDTO = servicePluginRegistry.getPluginFor(result.getEntityType()).get(
                result.getEntityId());
            doPublishEvent(new BlossomEntityUpdatedEvent<IBlossomServiceDTO>(BlossomAssociationGroupEntityServiceImpl.this, serviceDTO));
          } catch (Exception e) {
            logger.error("Error getting serviceDTO for entityType {} and id {}", result.getEntityType(),
                result.getEntityId());
            if (logger.isDebugEnabled()) {
              logger.debug("Error getting serviceDTO", e);
            }
          }

        }
      }, false, true, boAuthenticationUtilService.rightsForEntity(result.getEntityType(), BlossomRightPermissionEnum.READ));
    }

    doPublishEvent(new BlossomEntityCreatedEvent<BlossomAssociationGroupEntityServiceDTO>(this, result));

    return result;

  }

  @Transactional
  public BlossomAssociationGroupEntityServiceDTO update(Long groupEntityId,
      BlossomAssociationGroupEntityServiceDTO boAssociationGroupEntityServiceDTO) {

    BlossomAssociationGroupEntityProcessDTO boGroupEntityProcessDTO = boGroupEntityProcess.get(groupEntityId);

    boGroupEntityProcessDTO.setGroupId(boAssociationGroupEntityServiceDTO.getGroupId());
    boGroupEntityProcessDTO.setEntityId(boAssociationGroupEntityServiceDTO.getEntityId());
    boGroupEntityProcessDTO.setEntityType(boAssociationGroupEntityServiceDTO.getEntityType());

    BlossomAssociationGroupEntityProcessDTO updatedGroupEntityProcessDTO = boGroupEntityProcess
        .update(boGroupEntityProcessDTO);

    BlossomAssociationGroupEntityServiceDTO result = createServiceDTOfromProcessDTO(updatedGroupEntityProcessDTO);

    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomAssociationGroupEntityServiceDTO>(this, result));

    return result;
  }

  @Transactional
  public void delete(Long id) {
    final BlossomAssociationGroupEntityProcessDTO result = boGroupEntityProcess.get(id);
    BlossomAssociationGroupEntityServiceDTO entityDeleted = createServiceDTOfromProcessDTO(result);

    doPublishEvent(new BlossomEntityBeforeDeleteEvent<BlossomAssociationGroupEntityServiceDTO>(this, entityDeleted));

    super.delete(id);

    if (servicePluginRegistry.hasPluginFor(result.getEntityType())) {
      boAuthenticationUtilService.runAs(new Runnable() {
        public void run() {
          try {
            IBlossomServiceDTO serviceDTO = servicePluginRegistry.getPluginFor(result.getEntityType()).get(
                result.getEntityId());
            doPublishEvent(new BlossomEntityUpdatedEvent<IBlossomServiceDTO>(BlossomAssociationGroupEntityServiceImpl.this, serviceDTO));
          } catch (Exception e) {
            logger.error("Error getting serviceDTO for entityType {} and id {}", result.getEntityType(),
                result.getEntityId());
            if (logger.isDebugEnabled()) {
              logger.debug("Error getting serviceDTO", e);
            }
          }

        }
      }, false, true, boAuthenticationUtilService.rightsForEntity(result.getEntityType(), BlossomRightPermissionEnum.READ));
    }

    doPublishEvent(new BlossomEntityAfterDeleteEvent<BlossomAssociationGroupEntityServiceDTO>(this, entityDeleted));
  }

  public BlossomAssociationGroupEntityServiceDTO createServiceDTOfromProcessDTO(BlossomAssociationGroupEntityProcessDTO i) {
    if (i == null) {
      return null;
    }

    BlossomAssociationGroupEntityServiceDTO result = new BlossomAssociationGroupEntityServiceDTO();
    result.setId(i.getId());
    result.setEntityId(i.getEntityId());
    result.setEntityType(i.getEntityType());
    result.setGroupId(i.getGroupId());
    result.setDateCreation(i.getDateCreation());
    result.setDateModification(i.getDateModification());
    result.setUserCreation(i.getUserCreation());
    result.setUserModification(i.getUserModification());

    return result;
  }

  public BlossomAssociationGroupEntityProcessDTO createProcessDTOfromServiceDTO(BlossomAssociationGroupEntityServiceDTO o) {
    if (o == null) {
      return null;
    }

    BlossomAssociationGroupEntityProcessDTO result = new BlossomAssociationGroupEntityProcessDTO();
    result.setId(o.getId());
    result.setEntityId(o.getEntityId());
    result.setEntityType(o.getEntityType());
    result.setGroupId(o.getGroupId());
    result.setDateCreation(o.getDateCreation());
    result.setDateModification(o.getDateModification());
    result.setUserCreation(o.getUserCreation());
    result.setUserModification(o.getUserModification());

    return result;
  }

  public Page<BlossomAssociationGroupEntityServiceDTO> search(String query, Pageable pageable) {
    return null;
  }

  public Page<BlossomAssociationGroupEntityServiceDTO> search(String query, String associationName, String associationId,
      Pageable pageable) {
    return null;
  }

  public Page<BlossomAssociationGroupEntityServiceDTO> getAllByEntityIdAndEntityType(Pageable pageable, Long entityId,
      String entityType) {
    Page<BlossomAssociationGroupEntityProcessDTO> pagePO = boGroupEntityProcess.getAllByEntityIdAndEntityType(pageable,
        entityId, entityType);

    return pageServiceDTOFromPageProcessDTO(pagePO, pageable);
  }

  public Page<BlossomAssociationGroupEntityServiceDTO> getAllByEntityType(Pageable pageable, String entityType) {
    Page<BlossomAssociationGroupEntityProcessDTO> pagePO = boGroupEntityProcess.getAllByEntityType(pageable, entityType);
    return pageServiceDTOFromPageProcessDTO(pagePO, pageable);
  }

  public Page<BlossomAssociationGroupEntityServiceDTO> getAllByEntityTypeAndGroupId(Pageable pageable, String resourceType,
      Long groupId) {
    Page<BlossomAssociationGroupEntityProcessDTO> pagePO = boGroupEntityProcess.getAllByEntityTypeAndGroupId(pageable,
        resourceType, groupId);
    return pageServiceDTOFromPageProcessDTO(pagePO, pageable);
  }

  public Page<BlossomAssociationGroupEntityServiceDTO> getAllByEntityIdAndEntityTypeAndGroupId(Pageable pageable,
      Long entityId, String entityType, Long groupId) {
    Page<BlossomAssociationGroupEntityProcessDTO> pagePO = boGroupEntityProcess.getAllByEntityIdAndEntityTypeAndGroupId(
        pageable, entityId, entityType, groupId);
    return pageServiceDTOFromPageProcessDTO(pagePO, pageable);
  }

  @Transactional
  public void deleteByGroupIdAndEntityIdAndEntityType(Long groupId, Long entityId, String entityType) {
    boGroupEntityProcess.deleteByGroupIdAndEntityIdAndEntityType(groupId, entityId, entityType);
  }

  @Transactional
  public void deleteByEntityIdAndEntityType(Long entityId, String entityType) {
    boGroupEntityProcess.deleteByEntityIdAndEntityType(entityId, entityType);
  }

  public boolean supports(String delimiter) {
    return false;
  }

  @Transactional
  public void mergeAssociationsBetweenEntities(Long fromEntityId, String fromEntityType, Long toEntityId,
      String toEntityType) {
    Pageable pageable = new PageRequest(0, 100);
    Page<BlossomAssociationGroupEntityProcessDTO> pagePO = null;
    do {
      pagePO = boGroupEntityProcess.getAllByEntityIdAndEntityType(pageable, fromEntityId, fromEntityType);
      List<BlossomAssociationGroupEntityProcessDTO> assos = pagePO.getContent();
      for (BlossomAssociationGroupEntityProcessDTO asso : assos) {
        BlossomAssociationGroupEntityProcessDTO newAsso = new BlossomAssociationGroupEntityProcessDTO();
        newAsso.setEntityId(toEntityId);
        newAsso.setEntityType(toEntityType);
        newAsso.setGroupId(asso.getGroupId());
        boGroupEntityProcess.create(newAsso);
      }

      pageable = pageable.next();
    } while (pagePO.hasNext());
  }
}
