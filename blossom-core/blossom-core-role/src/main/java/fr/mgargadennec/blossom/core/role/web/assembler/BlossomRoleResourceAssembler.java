package fr.mgargadennec.blossom.core.role.web.assembler;

import java.io.Serializable;

import fr.mgargadennec.blossom.core.common.web.assembler.BlossomDefaultResourceAssembler;
import fr.mgargadennec.blossom.core.role.service.dto.BlossomRoleServiceDTO;
import fr.mgargadennec.blossom.core.role.web.controller.BlossomRoleController;
import fr.mgargadennec.blossom.core.role.web.resource.BlossomRoleHalResource;
import fr.mgargadennec.blossom.core.role.web.resource.BlossomRoleResourceState;

public class BlossomRoleResourceAssembler extends
    BlossomDefaultResourceAssembler<BlossomRoleServiceDTO, BlossomRoleResourceState, BlossomRoleHalResource> {

  public BlossomRoleResourceAssembler() {
    super(BlossomRoleController.class);
  }

  @Override
  protected Serializable getId(BlossomRoleServiceDTO entity) {
    return entity.getId();
  }

  @Override
  protected BlossomRoleHalResource createResource(BlossomRoleResourceState serviceDTO) {
    return new BlossomRoleHalResource(serviceDTO);
  }

  @Override
  public BlossomRoleResourceState toResourceState(BlossomRoleServiceDTO BlossomRoleServiceDTO) {
    BlossomRoleResourceState boRoleResource = new BlossomRoleResourceState();
    boRoleResource.setId(BlossomRoleServiceDTO.getId().toString());
    boRoleResource.setName(BlossomRoleServiceDTO.getName());
    boRoleResource.setDescription(BlossomRoleServiceDTO.getDescription());
    boRoleResource.setDateCreation(BlossomRoleServiceDTO.getDateCreation());
    boRoleResource.setDateModification(BlossomRoleServiceDTO.getDateModification());
    boRoleResource.setUserCreation(BlossomRoleServiceDTO.getUserCreation());
    boRoleResource.setUserModification(BlossomRoleServiceDTO.getUserModification());

    return boRoleResource;
  }

  @Override
  public BlossomRoleServiceDTO fromResourceState(BlossomRoleResourceState resource) {
    BlossomRoleServiceDTO result = new BlossomRoleServiceDTO();

    result.setId(resource.getId() == null ? null : Long.parseLong(resource.getId()));
    result.setName(resource.getName());
    result.setDescription(resource.getDescription());
    result.setDateCreation(resource.getDateCreation());
    result.setDateModification(resource.getDateModification());
    result.setUserCreation(resource.getUserCreation());
    result.setUserModification(resource.getUserModification());

    return result;
  }
}
