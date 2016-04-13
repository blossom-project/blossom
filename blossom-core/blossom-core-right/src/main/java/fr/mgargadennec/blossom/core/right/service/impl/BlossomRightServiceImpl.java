package fr.mgargadennec.blossom.core.right.service.impl;

import java.util.List;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Function;
import com.google.common.collect.FluentIterable;

import fr.mgargadennec.blossom.core.common.service.generic.impl.BlossomGenericServiceImpl;
import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityUpdatedEvent;
import fr.mgargadennec.blossom.core.right.constants.BlossomRightConst;
import fr.mgargadennec.blossom.core.right.process.IBlossomRightProcess;
import fr.mgargadennec.blossom.core.right.process.dto.BlossomRightProcessDTO;
import fr.mgargadennec.blossom.core.right.service.IBlossomRightService;
import fr.mgargadennec.blossom.core.right.service.dto.BlossomRightServiceDTO;

public class BlossomRightServiceImpl extends BlossomGenericServiceImpl<BlossomRightProcessDTO, BlossomRightServiceDTO> implements
    IBlossomRightService {

  private IBlossomRightProcess boRightProcess;
//  private IBlossomAssociationUserRoleProcess boAssociationUserRoleProcess;
//  private IBlossomUserProcess boUserProcess;

  public BlossomRightServiceImpl(
		  IBlossomRightProcess boRightProcess, 
//		  IBlossomAssociationUserRoleProcess boAssociationUserRoleProcess,
//		  IBlossomUserProcess boUserProcess, 
		  ApplicationEventPublisher eventPublisher
//		  ,
//		  IBlossomAuthenticationUtilService boAuthenticationUtilService
		  ) {
    super(boRightProcess, eventPublisher);
    this.boRightProcess = boRightProcess;
//    this.boAssociationUserRoleProcess = boAssociationUserRoleProcess;
//    this.boUserProcess = boUserProcess;
  }

  public Page<BlossomRightServiceDTO> getByRoleId(Pageable pageable, Long roleId) {
    Page<BlossomRightProcessDTO> boRightProcessDTOPage = boRightProcess.getByRoleId(pageable, roleId);

    List<BlossomRightServiceDTO> serviceDTOList = FluentIterable.from(boRightProcessDTOPage)
        .transform(new Function<BlossomRightProcessDTO, BlossomRightServiceDTO>() {
          public BlossomRightServiceDTO apply(BlossomRightProcessDTO boRightProcessDTO) {
            return createServiceDTOfromProcessDTO(boRightProcessDTO);
          }
        }).toList();

    return new PageImpl<BlossomRightServiceDTO>(serviceDTOList, pageable, boRightProcessDTOPage.getTotalElements());
  }

  public Page<BlossomRightServiceDTO> search(String query, Pageable pageable) {
    return null;
  }

  public Page<BlossomRightServiceDTO> search(String query, String associationName, String associationId, Pageable pageable) {
    return null;
  }

  @Transactional
  public BlossomRightServiceDTO create(BlossomRightServiceDTO boServiceDTO) {
    BlossomRightServiceDTO result = super.create(boServiceDTO);
    invalidateUsersToken(result.getRoleId());
    return result;
  }

  @Transactional
  public BlossomRightServiceDTO update(Long rightId, BlossomRightServiceDTO boRightUpdateServiceDTO) {
    BlossomRightProcessDTO boRightProcessDTOtoUpdate = boRightProcess.get(rightId);

    boRightProcessDTOtoUpdate.setPermissions(boRightUpdateServiceDTO.getPermissions());
    boRightProcessDTOtoUpdate.setResourceName(boRightUpdateServiceDTO.getResource());

    BlossomRightProcessDTO updatedRightProcessDTO = boRightProcess.update(boRightProcessDTOtoUpdate);
    BlossomRightServiceDTO result = createServiceDTOfromProcessDTO(updatedRightProcessDTO);

    invalidateUsersToken(result.getRoleId());
    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomRightServiceDTO>(this, result));

    return result;
  }

  @Transactional
  public void delete(Long rightId) {
    BlossomRightProcessDTO boRightProcessDTOtoUpdate = boRightProcess.get(rightId);
    Long roleId = boRightProcessDTOtoUpdate.getRoleId();

    super.delete(rightId);

    invalidateUsersToken(roleId);
  }

  public BlossomRightServiceDTO createServiceDTOfromProcessDTO(BlossomRightProcessDTO i) {
    if (i == null) {
      return null;
    }

    BlossomRightServiceDTO result = new BlossomRightServiceDTO();
    result.setId(i.getId());
    result.setDateCreation(i.getDateCreation());
    result.setDateModification(i.getDateModification());
    result.setUserCreation(i.getUserCreation());
    result.setUserModification(i.getUserModification());

    result.setPermissions(i.getPermissions());
    result.setResource(i.getResourceName());
    result.setRoleId(i.getRoleId());

    return result;
  }

  public BlossomRightProcessDTO createProcessDTOfromServiceDTO(BlossomRightServiceDTO o) {
    if (o == null) {
      return null;
    }

    BlossomRightProcessDTO result = new BlossomRightProcessDTO();
    result.setId(o.getId());
    result.setDateCreation(o.getDateCreation());
    result.setDateModification(o.getDateModification());
    result.setUserCreation(o.getUserCreation());
    result.setUserModification(o.getUserModification());

    result.setPermissions(o.getPermissions());
    result.setResourceName(o.getResource());
    result.setRoleId(o.getRoleId());

    return result;
  }

  protected void invalidateUsersToken(final Long roleId) {
//
//    BlossomRightServiceDTO[] neededRights = new BlossomRightServiceDTO[]{
//        boAuthenticationUtilService.right(BlossomConst.BO_USER_RIGHT_NAME, BlossomRightPermissionEnum.READ),
//        boAuthenticationUtilService.right(BlossomConst.BO_USER_ROLE_RIGHT_NAME, BlossomRightPermissionEnum.READ)};
//
//    boAuthenticationUtilService.runAs(new Runnable() {
//      public void run() {
//        Page<BlossomAssociationUserRoleProcessDTO> associations = boAssociationUserRoleProcess.getAllByRoleId(null, roleId);
//        if (associations != null) {
//
//          for (BlossomAssociationUserRoleProcessDTO boAssociationUserRoleServiceDTO : associations) {
//            BlossomUserProcessDTO user = boUserProcess.get(boAssociationUserRoleServiceDTO.getUserId());
//            doPublishEvent(new BlossomUserTokenInvalidationEvent(this, user.getLogin()));
//          }
//
//        }
//      }
//
//    }, false, true, neededRights);
  }

  public boolean supports(String delimiter) {
    return BlossomRightConst.BLOSSOM_RIGHT_ENTITY_NAME.equals(delimiter);
  }
}
