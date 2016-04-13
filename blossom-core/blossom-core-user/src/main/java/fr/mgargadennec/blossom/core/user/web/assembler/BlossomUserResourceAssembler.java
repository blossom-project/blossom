package fr.mgargadennec.blossom.core.user.web.assembler;

import java.io.Serializable;

import fr.mgargadennec.blossom.core.common.web.assembler.BlossomDefaultResourceAssembler;
import fr.mgargadennec.blossom.core.user.service.dto.BlossomUserServiceDTO;
import fr.mgargadennec.blossom.core.user.web.controller.BlossomUserController;
import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserHalResource;
import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserResourceState;

public class BlossomUserResourceAssembler extends
    BlossomDefaultResourceAssembler<BlossomUserServiceDTO, BlossomUserResourceState, BlossomUserHalResource> {

  public BlossomUserResourceAssembler() {
    super(BlossomUserController.class);
  }

  @Override
  public BlossomUserResourceState toResourceState(BlossomUserServiceDTO blossomUserServiceDTO) {
	  BlossomUserResourceState blossomUserResource = new BlossomUserResourceState();
    blossomUserResource.setId(blossomUserServiceDTO.getId().toString());
    blossomUserResource.setFirstname(blossomUserServiceDTO.getFirstname());
    blossomUserResource.setLastname(blossomUserServiceDTO.getLastname());
    blossomUserResource.setEmail(blossomUserServiceDTO.getEmail());
    blossomUserResource.setPhone(blossomUserServiceDTO.getPhone());
    blossomUserResource.setLogin(blossomUserServiceDTO.getLogin());
    blossomUserResource.setRoot(blossomUserServiceDTO.isRoot());
    blossomUserResource.setState(blossomUserServiceDTO.getState());
    blossomUserResource.setFunction(blossomUserServiceDTO.getFunction());
    blossomUserResource.setAvailableStates(blossomUserServiceDTO.getAvailableStates());
    blossomUserResource.setDateCreation(blossomUserServiceDTO.getDateCreation());
    blossomUserResource.setDateModification(blossomUserServiceDTO.getDateModification());
    blossomUserResource.setUserCreation(blossomUserServiceDTO.getUserCreation());
    blossomUserResource.setUserModification(blossomUserServiceDTO.getUserModification());

    return blossomUserResource;
  }

  @Override
  public BlossomUserServiceDTO fromResourceState(BlossomUserResourceState resource) {
    BlossomUserServiceDTO result = new BlossomUserServiceDTO();

    result.setId(resource.getId() == null ? null : Long.parseLong(resource.getId()));
    result.setFirstname(resource.getFirstname());
    result.setLastname(resource.getLastname());
    result.setEmail(resource.getEmail());
    result.setPhone(resource.getPhone());
    result.setLogin(resource.getLogin());
    result.setState(resource.getState());
    result.setFunction(resource.getFunction());
    result.setAvailableStates(resource.getAvailableStates());
    result.setDateCreation(resource.getDateCreation());
    result.setDateModification(resource.getDateModification());
    result.setUserCreation(resource.getUserCreation());
    result.setUserModification(resource.getUserModification());

    return result;
  }

  @Override
  protected BlossomUserHalResource createResource(BlossomUserResourceState resourceState) {
    return new BlossomUserHalResource(resourceState, null);
  }

  @Override
  protected Serializable getId(BlossomUserServiceDTO entity) {
    return entity.getId();
  }

}
