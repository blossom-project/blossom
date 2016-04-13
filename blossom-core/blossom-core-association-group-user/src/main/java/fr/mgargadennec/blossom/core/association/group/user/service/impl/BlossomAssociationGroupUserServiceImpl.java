package fr.mgargadennec.blossom.core.association.group.user.service.impl;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import fr.mgargadennec.blossom.core.association.group.user.constants.BlossomAssociationGroupUserConst;
import fr.mgargadennec.blossom.core.association.group.user.process.IBlossomAssociationGroupUserProcess;
import fr.mgargadennec.blossom.core.association.group.user.process.dto.BlossomAssociationGroupUserProcessDTO;
import fr.mgargadennec.blossom.core.association.group.user.service.IBlossomAssociationGroupUserService;
import fr.mgargadennec.blossom.core.association.group.user.service.dto.BlossomAssociationGroupUserServiceDTO;
import fr.mgargadennec.blossom.core.common.support.event.BlossomUserTokenInvalidationEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityAfterDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityBeforeDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityCreatedEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityUpdatedEvent;
import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.group.service.dto.BlossomGroupServiceDTO;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.core.user.service.dto.BlossomUserServiceDTO;
import fr.mgargadennec.blossom.security.core.service.impl.BlossomSecuredGenericServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomAssociationGroupUserServiceImpl extends
    BlossomSecuredGenericServiceImpl<BlossomAssociationGroupUserProcessDTO, BlossomAssociationGroupUserServiceDTO> implements
    IBlossomAssociationGroupUserService {

  private IBlossomAssociationGroupUserProcess boGroupUserProcess;
  private IBlossomGroupService boGroupService;
  private IBlossomUserService boUserService;

  public BlossomAssociationGroupUserServiceImpl(IBlossomAssociationGroupUserProcess boGroupUserProcess,
      IBlossomGroupService boGroupService, IBlossomUserService boUserService,
      IBlossomAuthenticationUtilService boAuthenticationUtilService, ApplicationEventPublisher eventPublisher) {
    super(boGroupUserProcess, boAuthenticationUtilService, eventPublisher);
    this.boGroupUserProcess = boGroupUserProcess;
    this.boGroupService = boGroupService;
    this.boUserService = boUserService;
  }

  public Page<BlossomAssociationGroupUserServiceDTO> getAll(Pageable pageable, Long userId, Long groupId) {

    Page<BlossomAssociationGroupUserProcessDTO> boAssociationGroupUserProcessDTOPage;

    if (userId != null && groupId == null) {
      boAssociationGroupUserProcessDTOPage = boGroupUserProcess.getAllByUserId(pageable, userId);
    } else if (groupId != null && userId == null) {
      boAssociationGroupUserProcessDTOPage = boGroupUserProcess.getAllByGroupId(pageable, groupId);
    } else if (groupId != null && userId != null) {
      boAssociationGroupUserProcessDTOPage = boGroupUserProcess.getAllByGroupIdAndUserId(pageable, groupId, userId);
    } else {
      boAssociationGroupUserProcessDTOPage = boGroupUserProcess.getAll(pageable);
    }

    List<BlossomAssociationGroupUserServiceDTO> serviceDTOList = FluentIterable.from(boAssociationGroupUserProcessDTOPage)
        .transform(new Function<BlossomAssociationGroupUserProcessDTO, BlossomAssociationGroupUserServiceDTO>() {
          public BlossomAssociationGroupUserServiceDTO apply(BlossomAssociationGroupUserProcessDTO associationGroupUser) {
            return createServiceDTOfromProcessDTO(associationGroupUser);
          }
        }).toList();

    return new PageImpl<BlossomAssociationGroupUserServiceDTO>(serviceDTOList, pageable,
        boAssociationGroupUserProcessDTOPage.getTotalElements());
  }

  @Transactional
  public BlossomAssociationGroupUserServiceDTO create(BlossomAssociationGroupUserServiceDTO boGroupUserServiceDTO) {

    BlossomUserServiceDTO boUser = boUserService.get(boGroupUserServiceDTO.getUserId());
    BlossomGroupServiceDTO boGroup = boGroupService.get(boGroupUserServiceDTO.getGroupId());

    if (boGroup == null) {
      throw new IllegalArgumentException("Error creating AssociationGroupUser, boGroup is null for id : "
          + boGroupUserServiceDTO.getGroupId());
    } else if (boUser == null) {
      throw new IllegalArgumentException("Error creating AssociationGroupUser, boUser is null for id : "
          + boGroupUserServiceDTO.getUserId());
    } else {
      BlossomAssociationGroupUserProcessDTO processDTOToCreate = createProcessDTOfromServiceDTO(boGroupUserServiceDTO);
      BlossomAssociationGroupUserProcessDTO createdProcessDTO = boGroupUserProcess.create(processDTOToCreate);

      BlossomAssociationGroupUserServiceDTO result = createServiceDTOfromProcessDTO(createdProcessDTO);

      invalidateUsersTokenByUserName(boUser.getLogin());

      doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));
      doPublishEvent(new BlossomEntityUpdatedEvent<BlossomGroupServiceDTO>(this, boGroup));
      doPublishEvent(new BlossomEntityCreatedEvent<BlossomAssociationGroupUserServiceDTO>(this, result));

      return result;
    }

  }

  @Transactional
  public BlossomAssociationGroupUserServiceDTO update(Long groupUserId,
      BlossomAssociationGroupUserServiceDTO boGroupUserUpdateServiceDTO) {

    BlossomAssociationGroupUserProcessDTO boGroupUserProcessDTO = boGroupUserProcess.get(groupUserId);

    boGroupUserProcessDTO.setGroupId(boGroupUserUpdateServiceDTO.getGroupId());
    boGroupUserProcessDTO.setUserId(boGroupUserUpdateServiceDTO.getUserId());

    BlossomAssociationGroupUserProcessDTO updatedGroupUserProcessDTO = boGroupUserProcess.update(boGroupUserProcessDTO);

    BlossomAssociationGroupUserServiceDTO result = createServiceDTOfromProcessDTO(updatedGroupUserProcessDTO);

    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomAssociationGroupUserServiceDTO>(this, result));

    return result;
  }

  @Transactional
  public void delete(Long id) {
    BlossomAssociationGroupUserProcessDTO result = boGroupUserProcess.get(id);

    super.delete(id);

    // Events
    BlossomGroupServiceDTO boGroup = boGroupService.get(result.getGroupId());
    BlossomUserServiceDTO boUser = boUserService.get(result.getUserId());
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomGroupServiceDTO>(this, boGroup));
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));
    invalidateUsersTokenByUserName(boUser.getLogin());
  }

  @Transactional
  public void deleteByGroupId(Long groupId) {
    Page<BlossomAssociationGroupUserProcessDTO> results = boGroupUserProcess.getAllByGroupId(null, groupId);

    if (results != null && results.getContent() != null) {
      for (BlossomAssociationGroupUserProcessDTO result : results.getContent()) {
        doPublishEvent(new BlossomEntityBeforeDeleteEvent<BlossomAssociationGroupUserServiceDTO>(this,
            createServiceDTOfromProcessDTO(result)));
      }
    }

    boGroupUserProcess.deleteByGroupId(groupId);

    BlossomGroupServiceDTO boGroup = boGroupService.get(groupId);
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomGroupServiceDTO>(this, boGroup));

    if (results != null && results.getContent() != null) {
      for (BlossomAssociationGroupUserProcessDTO result : results.getContent()) {
        // Events
        BlossomUserServiceDTO boUser = boUserService.get(result.getUserId());
        doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));

        doPublishEvent(new BlossomEntityAfterDeleteEvent<BlossomAssociationGroupUserServiceDTO>(this,
            createServiceDTOfromProcessDTO(result)));
        invalidateUsersTokenByUserName(boUser.getLogin());

      }
    }
  }

  @Transactional
  public void deleteByUserId(Long userId) {
    Page<BlossomAssociationGroupUserProcessDTO> results = boGroupUserProcess.getAllByUserId(null, userId);
    if (results != null && results.getContent() != null) {
      for (BlossomAssociationGroupUserProcessDTO result : results.getContent()) {
        doPublishEvent(new BlossomEntityBeforeDeleteEvent<BlossomAssociationGroupUserServiceDTO>(this,
            createServiceDTOfromProcessDTO(result)));
      }
    }

    boGroupUserProcess.deleteByUserId(userId);

    BlossomUserServiceDTO boUser = boUserService.get(userId);
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));
    if (boUser != null) {
      invalidateUsersTokenByUserName(boUser.getLogin());
    }
    if (results != null && results.getContent() != null) {
      for (BlossomAssociationGroupUserProcessDTO result : results.getContent()) {
        // Events
        BlossomGroupServiceDTO boGroup = boGroupService.get(result.getGroupId());
        doPublishEvent(new BlossomEntityUpdatedEvent<BlossomGroupServiceDTO>(this, boGroup));

        doPublishEvent(new BlossomEntityAfterDeleteEvent<BlossomAssociationGroupUserServiceDTO>(this,
            createServiceDTOfromProcessDTO(result)));

      }
    }
  }

  public Page<BlossomAssociationGroupUserServiceDTO> search(String query, Pageable pageable) {
    return null;
  }

  public Page<BlossomAssociationGroupUserServiceDTO> search(String query, String associationName, String associationId,
      Pageable pageable) {
    return null;
  }

  public BlossomAssociationGroupUserServiceDTO createServiceDTOfromProcessDTO(BlossomAssociationGroupUserProcessDTO i) {
    if (i == null) {
      return null;
    }

    BlossomAssociationGroupUserServiceDTO result = new BlossomAssociationGroupUserServiceDTO();
    result.setId(i.getId());
    result.setUserId(i.getUserId());
    result.setGroupId(i.getGroupId());
    result.setDateCreation(i.getDateCreation());
    result.setDateModification(i.getDateModification());
    result.setUserCreation(i.getUserCreation());
    result.setUserModification(i.getUserModification());

    return result;
  }

  public BlossomAssociationGroupUserProcessDTO createProcessDTOfromServiceDTO(BlossomAssociationGroupUserServiceDTO o) {
    if (o == null) {
      return null;
    }

    BlossomAssociationGroupUserProcessDTO result = new BlossomAssociationGroupUserProcessDTO();
    result.setId(o.getId());
    result.setUserId(o.getUserId());
    result.setGroupId(o.getGroupId());
    result.setDateCreation(o.getDateCreation());
    result.setDateModification(o.getDateModification());
    result.setUserCreation(o.getUserCreation());
    result.setUserModification(o.getUserModification());

    return result;
  }


  protected void invalidateUsersTokenByUserName(String username) {
    doPublishEvent(new BlossomUserTokenInvalidationEvent(this, username));
  }

  public boolean supports(String delimiter) {
    return BlossomAssociationGroupUserConst.BLOSSOM_ASSOCIATION_GROUP_USER_ENTITY_NAME.equals(delimiter);
  }
}
