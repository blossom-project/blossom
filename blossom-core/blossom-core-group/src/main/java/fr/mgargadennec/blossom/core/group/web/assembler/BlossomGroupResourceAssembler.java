package fr.mgargadennec.blossom.core.group.web.assembler;

import java.io.Serializable;

import fr.mgargadennec.blossom.core.common.web.assembler.BlossomDefaultResourceAssembler;
import fr.mgargadennec.blossom.core.group.service.dto.BlossomGroupServiceDTO;
import fr.mgargadennec.blossom.core.group.web.controller.BlossomGroupController;
import fr.mgargadennec.blossom.core.group.web.resource.BlossomGroupHalResource;
import fr.mgargadennec.blossom.core.group.web.resource.BlossomGroupResourceState;

public class BlossomGroupResourceAssembler extends
    BlossomDefaultResourceAssembler<BlossomGroupServiceDTO, BlossomGroupResourceState, BlossomGroupHalResource> {

  public BlossomGroupResourceAssembler() {
    super(BlossomGroupController.class);
  }

  @Override
  public BlossomGroupResourceState toResourceState(BlossomGroupServiceDTO BlossomGroupServiceDTO) {
    BlossomGroupResourceState boGroupResource = new BlossomGroupResourceState();
    boGroupResource.setId(BlossomGroupServiceDTO.getId().toString());
    boGroupResource.setName(BlossomGroupServiceDTO.getName());
    boGroupResource.setDescription(BlossomGroupServiceDTO.getDescription());
    boGroupResource.setDateCreation(BlossomGroupServiceDTO.getDateCreation());
    boGroupResource.setDateModification(BlossomGroupServiceDTO.getDateModification());
    boGroupResource.setUserCreation(BlossomGroupServiceDTO.getUserCreation());
    boGroupResource.setUserModification(BlossomGroupServiceDTO.getUserModification());

    return boGroupResource;
  }

  @Override
  public BlossomGroupServiceDTO fromResourceState(BlossomGroupResourceState resource) {
    BlossomGroupServiceDTO result = new BlossomGroupServiceDTO();
    result.setId(resource.getId() == null ? null : Long.parseLong(resource.getId()));
    result.setName(resource.getName());
    result.setDescription(resource.getDescription());
    result.setDateCreation(resource.getDateCreation());
    result.setDateModification(resource.getDateModification());
    result.setUserCreation(resource.getUserCreation());
    result.setUserModification(resource.getUserModification());

    return result;
  }

  @Override
  protected BlossomGroupHalResource createResource(BlossomGroupResourceState resourceState) {
    return new BlossomGroupHalResource(resourceState);
  }

  @Override
  protected Serializable getId(BlossomGroupServiceDTO entity) {
    return entity.getId();
  }

}
