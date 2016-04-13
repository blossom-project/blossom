package fr.mgargadennec.blossom.core.association.user.role.web.assembler;

import java.io.Serializable;

import fr.mgargadennec.blossom.core.association.user.role.web.controller.BlossomRightController;
import fr.mgargadennec.blossom.core.association.user.role.web.resource.BlossomRightHalResource;
import fr.mgargadennec.blossom.core.association.user.role.web.resource.BlossomRightResourceState;
import fr.mgargadennec.blossom.core.common.web.assembler.BlossomDefaultResourceAssembler;
import fr.mgargadennec.blossom.core.right.service.dto.BlossomRightServiceDTO;

public class BlossomRightResourceAssembler extends
		BlossomDefaultResourceAssembler<BlossomRightServiceDTO, BlossomRightResourceState, BlossomRightHalResource> {

	public BlossomRightResourceAssembler() {
		super(BlossomRightController.class);
	}

	@Override
	protected Serializable getId(BlossomRightServiceDTO entity) {
		return entity.getId();
	}

	@Override
	public BlossomRightHalResource toResource(BlossomRightServiceDTO entity) {
		return createResourceWithId(getId(entity), entity, entity.getRoleId());
	}

	@Override
	protected BlossomRightHalResource createResource(BlossomRightResourceState serviceDTO) {
		return new BlossomRightHalResource(serviceDTO);
	}

	@Override
	public BlossomRightResourceState toResourceState(BlossomRightServiceDTO boRightServiceDTO) {
		BlossomRightResourceState boRightResource = new BlossomRightResourceState();
		boRightResource.setId(boRightServiceDTO.getId().toString());
		boRightResource.setDateCreation(boRightServiceDTO.getDateCreation());
		boRightResource.setDateModification(boRightServiceDTO.getDateModification());
		boRightResource.setUserCreation(boRightServiceDTO.getUserCreation());
		boRightResource.setUserModification(boRightServiceDTO.getUserModification());

		boRightResource.setPermissions(boRightServiceDTO.getPermissions());
		boRightResource.setResource(boRightServiceDTO.getResource());
		boRightResource.setRoleId(boRightServiceDTO.getRoleId().toString());

		return boRightResource;
	}

	@Override
	public BlossomRightServiceDTO fromResourceState(BlossomRightResourceState resource) {

		BlossomRightServiceDTO result = new BlossomRightServiceDTO();

		result.setId(resource.getId() == null ? null : Long.parseLong(resource.getId()));
		result.setDateCreation(resource.getDateCreation());
		result.setDateModification(resource.getDateModification());
		result.setUserCreation(resource.getUserCreation());
		result.setUserModification(resource.getUserModification());

		result.setPermissions(resource.getPermissions());
		result.setResource(resource.getResource());
		result.setRoleId(resource.getRoleId() == null ? null : Long.parseLong(resource.getRoleId()));

		return result;
	}
}
