package fr.mgargadennec.blossom.core.association.group.user.web.assembler;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;

import fr.mgargadennec.blossom.core.association.group.user.service.dto.BlossomAssociationGroupUserServiceDTO;
import fr.mgargadennec.blossom.core.association.group.user.web.controller.BlossomAssociationGroupUserController;
import fr.mgargadennec.blossom.core.association.group.user.web.resource.BlossomAssociationGroupUserHalResource;
import fr.mgargadennec.blossom.core.association.group.user.web.resource.BlossomAssociationGroupUserResourceState;
import fr.mgargadennec.blossom.core.common.web.assembler.BlossomDefaultResourceAssembler;
import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.group.service.dto.BlossomGroupServiceDTO;
import fr.mgargadennec.blossom.core.group.web.assembler.BlossomGroupResourceAssembler;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.core.user.service.dto.BlossomUserServiceDTO;
import fr.mgargadennec.blossom.core.user.web.assembler.BlossomUserResourceAssembler;

public class BlossomAssociationGroupUserResourceAssembler extends
    BlossomDefaultResourceAssembler<BlossomAssociationGroupUserServiceDTO, BlossomAssociationGroupUserResourceState, BlossomAssociationGroupUserHalResource> {

  private BlossomUserResourceAssembler blossomUserResourceAssembler;
  private BlossomGroupResourceAssembler blossomGroupResourceAssembler;
  private IBlossomUserService blossomUserService;
  private IBlossomGroupService blossomGroupService;

  public BlossomAssociationGroupUserResourceAssembler(
		  BlossomUserResourceAssembler blossomUserResourceAssembler,
		  BlossomGroupResourceAssembler blossomGroupResourceAssembler, 
		  IBlossomUserService blossomUserService, 
		  IBlossomGroupService blossomGroupService) {
    super(BlossomAssociationGroupUserController.class);
    this.blossomUserResourceAssembler = blossomUserResourceAssembler;
    this.blossomGroupResourceAssembler = blossomGroupResourceAssembler;
    this.blossomUserService = blossomUserService;
    this.blossomGroupService = blossomGroupService;
  }

  @Override
  public BlossomAssociationGroupUserHalResource toResource(BlossomAssociationGroupUserServiceDTO BlossomAssociationGroupUserServiceDTO) {
    BlossomAssociationGroupUserHalResource resource = super.toResource(BlossomAssociationGroupUserServiceDTO);

    BlossomGroupServiceDTO boGroupServiceDTO = blossomGroupService.get(BlossomAssociationGroupUserServiceDTO.getGroupId());
    BlossomUserServiceDTO boUserServiceDTO = blossomUserService.get(BlossomAssociationGroupUserServiceDTO.getUserId());

    EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
    List<EmbeddedWrapper> embeddeds = Arrays.asList(
        wrappers.wrap(blossomUserResourceAssembler.toResource(boUserServiceDTO)),
        wrappers.wrap(blossomGroupResourceAssembler.toResource(boGroupServiceDTO)));

    Resources<EmbeddedWrapper> embedded = new Resources(embeddeds);
    resource.setEmbeds(embedded);

    return resource;
  }

  @Override
  protected Serializable getId(BlossomAssociationGroupUserServiceDTO entity) {
    return entity.getId();
  }

  @Override
  protected BlossomAssociationGroupUserHalResource createResource(BlossomAssociationGroupUserResourceState serviceDTO) {
    return new BlossomAssociationGroupUserHalResource(serviceDTO);
  }

  @Override
  public BlossomAssociationGroupUserResourceState toResourceState(BlossomAssociationGroupUserServiceDTO BlossomAssociationGroupUserServiceDTO) {
    BlossomAssociationGroupUserResourceState boAssociationGroupUserResource = new BlossomAssociationGroupUserResourceState();
    boAssociationGroupUserResource.setId(BlossomAssociationGroupUserServiceDTO.getId().toString());
    boAssociationGroupUserResource.setUserId(BlossomAssociationGroupUserServiceDTO.getUserId().toString());
    boAssociationGroupUserResource.setGroupId(BlossomAssociationGroupUserServiceDTO.getGroupId().toString());
    boAssociationGroupUserResource.setDateCreation(BlossomAssociationGroupUserServiceDTO.getDateCreation());
    boAssociationGroupUserResource.setDateModification(BlossomAssociationGroupUserServiceDTO.getDateModification());
    boAssociationGroupUserResource.setUserCreation(BlossomAssociationGroupUserServiceDTO.getUserCreation());
    boAssociationGroupUserResource.setUserModification(BlossomAssociationGroupUserServiceDTO.getUserModification());
    return boAssociationGroupUserResource;
  }

  @Override
  public BlossomAssociationGroupUserServiceDTO fromResourceState(BlossomAssociationGroupUserResourceState resource) {
    BlossomAssociationGroupUserServiceDTO result = new BlossomAssociationGroupUserServiceDTO();

    result.setId(resource.getId() == null ? null : Long.parseLong(resource.getId()));
    result.setUserId(Long.parseLong(resource.getUserId()));
    result.setGroupId(Long.parseLong(resource.getGroupId()));
    result.setDateCreation(resource.getDateCreation());
    result.setDateModification(resource.getDateModification());
    result.setUserCreation(resource.getUserCreation());
    result.setUserModification(resource.getUserModification());

    return result;
  }
}
