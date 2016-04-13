package fr.mgargadennec.blossom.core.association.user.role.service.impl;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import fr.mgargadennec.blossom.core.association.user.role.constants.BlossomAssociationUserRoleConst;
import fr.mgargadennec.blossom.core.association.user.role.process.IBlossomAssociationUserRoleProcess;
import fr.mgargadennec.blossom.core.association.user.role.process.dto.BlossomAssociationUserRoleProcessDTO;
import fr.mgargadennec.blossom.core.association.user.role.service.IBlossomAssociationUserRoleService;
import fr.mgargadennec.blossom.core.association.user.role.service.dto.BlossomAssociationUserRoleServiceDTO;
import fr.mgargadennec.blossom.core.common.support.event.BlossomUserTokenInvalidationEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityAfterDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityBeforeDeleteEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityCreatedEvent;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityUpdatedEvent;
import fr.mgargadennec.blossom.core.role.service.IBlossomRoleService;
import fr.mgargadennec.blossom.core.role.service.dto.BlossomRoleServiceDTO;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.core.user.service.dto.BlossomUserServiceDTO;
import fr.mgargadennec.blossom.security.core.service.impl.BlossomSecuredGenericServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomAssociationUserRoleServiceImpl extends
BlossomSecuredGenericServiceImpl<BlossomAssociationUserRoleProcessDTO, BlossomAssociationUserRoleServiceDTO> implements
IBlossomAssociationUserRoleService {

  private IBlossomAssociationUserRoleProcess boUserRoleProcess;
  private IBlossomUserService boUserService;
  private IBlossomRoleService boRoleService;

  public BlossomAssociationUserRoleServiceImpl(IBlossomAssociationUserRoleProcess boUserRoleProcess,
      IBlossomUserService boUserService, IBlossomRoleService boRoleService,
      IBlossomAuthenticationUtilService boAuthenticationUtilService, ApplicationEventPublisher eventPublisher) {
    super(boUserRoleProcess, boAuthenticationUtilService, eventPublisher);
    this.boUserRoleProcess = boUserRoleProcess;
    this.boUserService = boUserService;
    this.boRoleService = boRoleService;
  }

  public Page<BlossomAssociationUserRoleServiceDTO> search(String query, Pageable pageable) {
    return null;
  }

  public Page<BlossomAssociationUserRoleServiceDTO> search(String query, String associationName, String associationId,
      Pageable pageable) {
    return null;
  }

  public Page<BlossomAssociationUserRoleServiceDTO> getAll(Pageable pageable, Long roleId, Long userId) {

    Page<BlossomAssociationUserRoleProcessDTO> boAssociationUserRoleProcessDTOPage;

    if (roleId != null && userId == null) {
      boAssociationUserRoleProcessDTOPage = boUserRoleProcess.getAllByRoleId(pageable, roleId);
    } else if (userId != null && roleId == null) {
      boAssociationUserRoleProcessDTOPage = boUserRoleProcess.getAllByUserId(pageable, userId);
    } else if (userId != null && roleId != null) {
      boAssociationUserRoleProcessDTOPage = boUserRoleProcess.getAllByUserIdAndRoleId(pageable, userId, roleId);
    } else {
      boAssociationUserRoleProcessDTOPage = boUserRoleProcess.getAll(pageable);
    }

    List<BlossomAssociationUserRoleServiceDTO> serviceDTOList = FluentIterable.from(boAssociationUserRoleProcessDTOPage)
        .transform(new Function<BlossomAssociationUserRoleProcessDTO, BlossomAssociationUserRoleServiceDTO>() {
          public BlossomAssociationUserRoleServiceDTO apply(BlossomAssociationUserRoleProcessDTO associationUserRole) {
            return createServiceDTOfromProcessDTO(associationUserRole);
          }
        }).toList();

    return new PageImpl<BlossomAssociationUserRoleServiceDTO>(serviceDTOList, pageable,
        boAssociationUserRoleProcessDTOPage.getTotalElements());
  }

  @Override
  @Transactional
  public BlossomAssociationUserRoleServiceDTO create(BlossomAssociationUserRoleServiceDTO boUserRoleServiceDTO) {

    BlossomRoleServiceDTO boRole = boRoleService.get(boUserRoleServiceDTO.getRoleId());
    BlossomUserServiceDTO boUser = boUserService.get(boUserRoleServiceDTO.getUserId());

    if (boUser == null) {
      throw new IllegalArgumentException("Error creating AssociationUserRole, boUser is null for id : "
          + boUserRoleServiceDTO.getUserId());
    } else if (boRole == null) {
      throw new IllegalArgumentException("Error creating AssociationUserRole, boRole is null for id : "
          + boUserRoleServiceDTO.getRoleId());
    }

    BlossomAssociationUserRoleProcessDTO processDTOToCreate = createProcessDTOfromServiceDTO(boUserRoleServiceDTO);
    BlossomAssociationUserRoleProcessDTO createdProcessDTO = boUserRoleProcess.create(processDTOToCreate);
    BlossomAssociationUserRoleServiceDTO result = createServiceDTOfromProcessDTO(createdProcessDTO);

    invalidateUsersTokenByUserName(boUser.getLogin());

    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomRoleServiceDTO>(this, boRole));
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));
    doPublishEvent(new BlossomEntityCreatedEvent<BlossomAssociationUserRoleServiceDTO>(this, result));

    return result;
  }

  @Transactional
  public BlossomAssociationUserRoleServiceDTO update(Long userRoleId,
      BlossomAssociationUserRoleServiceDTO boUserRoleUpdateServiceDTO) {

    BlossomAssociationUserRoleProcessDTO boUserRoleProcessDTO = boUserRoleProcess.get(userRoleId);

    boUserRoleProcessDTO.setUserId(boUserRoleUpdateServiceDTO.getUserId());
    boUserRoleProcessDTO.setRoleId(boUserRoleUpdateServiceDTO.getRoleId());

    BlossomAssociationUserRoleProcessDTO updatedProcessDTO = boUserRoleProcess.update(boUserRoleProcessDTO);

    BlossomAssociationUserRoleServiceDTO result = createServiceDTOfromProcessDTO(updatedProcessDTO);

    invalidateUsersTokenByUserId(result.getUserId());

    // Events
    BlossomRoleServiceDTO boRole = boRoleService.get(boUserRoleUpdateServiceDTO.getRoleId());
    BlossomUserServiceDTO boUser = boUserService.get(boUserRoleUpdateServiceDTO.getUserId());
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomRoleServiceDTO>(this, boRole));
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomAssociationUserRoleServiceDTO>(this, result));

    return result;
  }

  @Override
  @Transactional
  public void delete(Long id) {
    BlossomAssociationUserRoleProcessDTO result = boUserRoleProcess.get(id);
    Long userId = result.getUserId();

    super.delete(id);

    // Events
    BlossomRoleServiceDTO boRole = boRoleService.get(result.getRoleId());
    BlossomUserServiceDTO boUser = boUserService.get(result.getUserId());
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomRoleServiceDTO>(this, boRole));
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));

    invalidateUsersTokenByUserId(userId);
  }

  @Transactional
  public void deleteByUserId(Long userId) {
    Page<BlossomAssociationUserRoleProcessDTO> results = boUserRoleProcess.getAllByUserId(null, userId);
    if (results != null && results.getContent() != null) {
      for (BlossomAssociationUserRoleProcessDTO result : results.getContent()) {
        doPublishEvent(new BlossomEntityBeforeDeleteEvent<BlossomAssociationUserRoleServiceDTO>(this,
            createServiceDTOfromProcessDTO(result)));
      }
    }

    boUserRoleProcess.deleteByUserId(userId);

    BlossomUserServiceDTO boUser = boUserService.get(userId);
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));

    if (results != null && results.getContent() != null) {
      for (BlossomAssociationUserRoleProcessDTO result : results.getContent()) {
        // Events
        BlossomRoleServiceDTO boRole = boRoleService.get(result.getRoleId());
        doPublishEvent(new BlossomEntityUpdatedEvent<BlossomRoleServiceDTO>(this, boRole));

        doPublishEvent(new BlossomEntityAfterDeleteEvent<BlossomAssociationUserRoleServiceDTO>(this,
            createServiceDTOfromProcessDTO(result)));
      }
    }

    invalidateUsersTokenByUserId(userId);
  }

  @Transactional
  public void deleteByRoleId(Long roleId) {
    Page<BlossomAssociationUserRoleProcessDTO> results = boUserRoleProcess.getAllByRoleId(null, roleId);
    if (results != null && results.getContent() != null) {
      for (BlossomAssociationUserRoleProcessDTO result : results.getContent()) {
        doPublishEvent(new BlossomEntityBeforeDeleteEvent<BlossomAssociationUserRoleServiceDTO>(this,
            createServiceDTOfromProcessDTO(result)));
      }
    }
    boUserRoleProcess.deleteByRoleId(roleId);

    BlossomRoleServiceDTO boRole = boRoleService.get(roleId);
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomRoleServiceDTO>(this, boRole));

    if (results != null && results.getContent() != null) {
      for (BlossomAssociationUserRoleProcessDTO result : results.getContent()) {
        // Events
        BlossomUserServiceDTO boUser = boUserService.get(result.getUserId());
        doPublishEvent(new BlossomEntityUpdatedEvent<BlossomUserServiceDTO>(this, boUser));

        doPublishEvent(new BlossomEntityAfterDeleteEvent<BlossomAssociationUserRoleServiceDTO>(this,
            createServiceDTOfromProcessDTO(result)));
      }
    }
    invalidateUsersTokenByRoleId(roleId);
  }

  public BlossomAssociationUserRoleServiceDTO createServiceDTOfromProcessDTO(BlossomAssociationUserRoleProcessDTO i) {
    if (i == null) {
      return null;
    }

    BlossomAssociationUserRoleServiceDTO result = new BlossomAssociationUserRoleServiceDTO();
    result.setId(i.getId());
    result.setUserId(i.getUserId());
    result.setRoleId(i.getRoleId());
    result.setDateCreation(i.getDateCreation());
    result.setDateModification(i.getDateModification());
    result.setUserCreation(i.getUserCreation());
    result.setUserModification(i.getUserModification());

    return result;
  }

  public BlossomAssociationUserRoleProcessDTO createProcessDTOfromServiceDTO(BlossomAssociationUserRoleServiceDTO o) {
    if (o == null) {
      return null;
    }

    BlossomAssociationUserRoleProcessDTO result = new BlossomAssociationUserRoleProcessDTO();
    result.setId(o.getId());
    result.setUserId(o.getUserId());
    result.setRoleId(o.getRoleId());
    result.setDateCreation(o.getDateCreation());
    result.setDateModification(o.getDateModification());
    result.setUserCreation(o.getUserCreation());
    result.setUserModification(o.getUserModification());

    return result;
  }

  /**
   * R�cup�re tout les users qui ont ce r�le et invalide leur token
   *
   * @param roleId
   */
  protected void invalidateUsersTokenByRoleId(Long roleId) {

    Page<BlossomAssociationUserRoleProcessDTO> associations = boUserRoleProcess.getAllByRoleId(null, roleId);
    if (associations != null) {

      for (BlossomAssociationUserRoleProcessDTO boAssociationUserRoleServiceDTO : associations) {
        invalidateUsersTokenByUserId(boAssociationUserRoleServiceDTO.getUserId());
      }

    }

  }

  protected void invalidateUsersTokenByUserId(Long userID) {
    BlossomUserServiceDTO user = boUserService.get(userID);
    if (user != null) {
      invalidateUsersTokenByUserName(user.getLogin());
    }
  }

  protected void invalidateUsersTokenByUserName(String username) {
    doPublishEvent(new BlossomUserTokenInvalidationEvent(this, username));
  }

  public boolean supports(String delimiter) {
    return BlossomAssociationUserRoleConst.BLOSSOM_ASSOCIATION_USER_ROLE_ENTITY_NAME.equals(delimiter);
  }

}
