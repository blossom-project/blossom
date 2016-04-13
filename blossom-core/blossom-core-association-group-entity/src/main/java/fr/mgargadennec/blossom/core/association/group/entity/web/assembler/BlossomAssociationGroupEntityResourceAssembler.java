package fr.mgargadennec.blossom.core.association.group.entity.web.assembler;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.core.EmbeddedWrapper;
import org.springframework.hateoas.core.EmbeddedWrappers;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.mvc.ResourceAssemblerSupport;

import fr.mgargadennec.blossom.core.association.group.entity.service.dto.BlossomAssociationGroupEntityServiceDTO;
import fr.mgargadennec.blossom.core.association.group.entity.web.resource.BlossomAssociationGroupEntityHalResource;
import fr.mgargadennec.blossom.core.association.group.entity.web.resource.BlossomAssociationGroupEntityResourceState;
import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericReadService;
import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.group.service.dto.BlossomGroupServiceDTO;
import fr.mgargadennec.blossom.core.group.web.assembler.BlossomGroupResourceAssembler;

public class BlossomAssociationGroupEntityResourceAssembler<Y extends IBlossomServiceDTO> extends
    ResourceAssemblerSupport<BlossomAssociationGroupEntityServiceDTO, BlossomAssociationGroupEntityHalResource> {

  private ResourceAssembler<Y, ?> entityResourceAssembler;

  private BlossomGroupResourceAssembler boGroupResourceAssembler;

  private IBlossomGenericReadService<?, Y> entityService;

  private IBlossomGroupService blossomGroupService;

  @Autowired(required = false)
  private CurieProvider curieProvider;

  public BlossomAssociationGroupEntityResourceAssembler(Class<?> controllerClass, BlossomGroupResourceAssembler boGroupResourceAssembler,
      IBlossomGroupService boGroupService, ResourceAssembler<Y, ?> entityResourceAssembler,
      IBlossomGenericReadService<?, Y> entityService) {
    super(controllerClass, BlossomAssociationGroupEntityHalResource.class);
    this.entityResourceAssembler = entityResourceAssembler;
    this.boGroupResourceAssembler = boGroupResourceAssembler;
    this.entityService = entityService;
    this.blossomGroupService = boGroupService;
  }

  protected BlossomAssociationGroupEntityHalResource createResource(BlossomAssociationGroupEntityResourceState serviceDTO) {
    return new BlossomAssociationGroupEntityHalResource(serviceDTO);
  }

  public BlossomAssociationGroupEntityResourceState toResourceState(BlossomAssociationGroupEntityServiceDTO entity) {
    BlossomAssociationGroupEntityResourceState resource = new BlossomAssociationGroupEntityResourceState();
    resource.setId(entity.getId().toString());
    resource.setDateCreation(entity.getDateCreation());
    resource.setDateModification(entity.getDateModification());
    resource.setUserCreation(entity.getUserCreation());
    resource.setUserModification(entity.getUserModification());

    resource.setGroupId(entity.getGroupId().toString());
    resource.setEntityId(entity.getEntityId().toString());

    return resource;
  }

  public BlossomAssociationGroupEntityServiceDTO fromResourceState(BlossomAssociationGroupEntityResourceState resource, String entityType) {
	  BlossomAssociationGroupEntityServiceDTO result = new BlossomAssociationGroupEntityServiceDTO();

    result.setId(resource.getId() == null ? null : Long.parseLong(resource.getId()));
    result.setDateCreation(resource.getDateCreation());
    result.setDateModification(resource.getDateModification());
    result.setUserCreation(resource.getUserCreation());
    result.setUserModification(resource.getUserModification());

    result.setEntityType(entityType);
    result.setGroupId(resource.getGroupId() == null ? null : Long.parseLong(resource.getGroupId()));
    result.setEntityId(resource.getEntityId() == null ? null : Long.parseLong(resource.getEntityId()));

    return result;
  }

  @Override
  protected BlossomAssociationGroupEntityHalResource instantiateResource(BlossomAssociationGroupEntityServiceDTO entity) {
    return createResource(toResourceState(entity));
  }

  @Override
  public BlossomAssociationGroupEntityHalResource toResource(BlossomAssociationGroupEntityServiceDTO entity) {
    BlossomAssociationGroupEntityHalResource resource = createResourceWithId(entity.getId(), entity);

    // Embedded objects
    BlossomGroupServiceDTO boGroupServiceDTO = blossomGroupService.get(entity.getGroupId());
    Y abstractBoServiceDTO = entityService.get(entity.getEntityId());

    EmbeddedWrappers wrappers = new EmbeddedWrappers(false);
    String entityRel = "entity";
    if (curieProvider != null) {
      entityRel = curieProvider.getNamespacedRelFor(entityRel);
    }

    List<EmbeddedWrapper> embeddeds = Arrays.asList(
        wrappers.wrap(entityResourceAssembler.toResource(abstractBoServiceDTO), entityRel),
        wrappers.wrap(boGroupResourceAssembler.toResource(boGroupServiceDTO)));

    Resources<EmbeddedWrapper> embedded = new Resources(embeddeds);
    resource.setEmbeds(embedded);

    return resource;
  }

}
