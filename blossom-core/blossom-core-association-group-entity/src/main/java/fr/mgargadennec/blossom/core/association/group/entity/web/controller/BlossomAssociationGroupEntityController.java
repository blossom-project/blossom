package fr.mgargadennec.blossom.core.association.group.entity.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.mail.MessagingException;

import org.apache.commons.lang3.text.WordUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.TemplateVariable;
import org.springframework.hateoas.TemplateVariables;
import org.springframework.hateoas.UriTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.mgargadennec.blossom.core.association.group.entity.service.IBlossomAssociationGroupEntityService;
import fr.mgargadennec.blossom.core.association.group.entity.service.dto.BlossomAssociationGroupEntityServiceDTO;
import fr.mgargadennec.blossom.core.association.group.entity.web.assembler.BlossomAssociationGroupEntityResourceAssembler;
import fr.mgargadennec.blossom.core.association.group.entity.web.resource.BlossomAssociationGroupEntityHalResource;
import fr.mgargadennec.blossom.core.association.group.entity.web.resource.BlossomAssociationGroupEntityResourceState;
import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;
import fr.mgargadennec.blossom.core.group.web.controller.BlossomGroupController;
import fr.mgargadennec.blossom.core.group.web.resource.BlossomGroupHalResource;

@Controller
@ExposesResourceFor(BlossomAssociationGroupEntityHalResource.class)
public abstract class BlossomAssociationGroupEntityController<X extends IBlossomServiceDTO> {

	IBlossomAssociationGroupEntityService authorizationService;

	EntityLinks entityLinks;

	RelProvider relProvider;

	BlossomAssociationGroupEntityResourceAssembler<X> assembler;

	String resourceType;

	public BlossomAssociationGroupEntityController(String resourceType,
			IBlossomAssociationGroupEntityService authorizationService, EntityLinks entityLinks,
			RelProvider relProvider, BlossomAssociationGroupEntityResourceAssembler<X> assembler) {
		this.resourceType = resourceType;
		this.assembler = assembler;
		this.authorizationService = authorizationService;
		this.entityLinks = entityLinks;
		this.relProvider = relProvider;

	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody PagedResources<BlossomAssociationGroupEntityHalResource> scopes(
			@PageableDefault Pageable pageable, @RequestParam(value = "entityId", required = false) Long entityId,
			@RequestParam(value = "groupId", required = false) Long groupId,
			PagedResourcesAssembler<BlossomAssociationGroupEntityServiceDTO> pagedResourcesAssembler) {

		Link link = linkTo(methodOn(this.getClass()).scopes(pageable, entityId, groupId, pagedResourcesAssembler))
				.withSelfRel();

		Page<BlossomAssociationGroupEntityServiceDTO> page = null;

		if (entityId == null && groupId != null) {
			page = authorizationService.getAllByEntityTypeAndGroupId(pageable, resourceType, groupId);
		} else if (entityId != null && groupId == null) {
			page = authorizationService.getAllByEntityIdAndEntityType(pageable, entityId, resourceType);
		} else if (entityId != null && groupId != null) {
			page = authorizationService.getAllByEntityIdAndEntityTypeAndGroupId(pageable, entityId, resourceType,
					groupId);
		} else {
			page = authorizationService.getAllByEntityType(pageable, resourceType);
		}

		PagedResources<BlossomAssociationGroupEntityHalResource> pagedResources = pagedResourcesAssembler
				.toResource(page, assembler, link);

		UriTemplate uriTemplate = new UriTemplate(linkTo(this.getClass()).toUri().toString() + "/{id}");
		pagedResources.add(new Link(uriTemplate, "authorization"));

		UriTemplate searchUri = new UriTemplate(linkTo(methodOn(this.getClass()).scopes(null, null, null, null))
				.toUriComponentsBuilder().build().toUriString(), getTemplateVariables());
		pagedResources.add(new Link(searchUri, "pagedAuthorizations"));

		UriTemplate groupsUri = new UriTemplate(linkTo(methodOn(BlossomGroupController.class).groups(null, null, null))
				.toUriComponentsBuilder().build().toUriString(), BlossomGroupController.getTemplateVariables());
		pagedResources.add(new Link(groupsUri, "paged"
				+ WordUtils.capitalize(relProvider.getCollectionResourceRelFor(BlossomGroupHalResource.class))));

		return pagedResources;
	}

	@RequestMapping(value = "/{authorizationId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BlossomAssociationGroupEntityHalResource get(@PathVariable("authorizationId") Long authorizationId) {
		return assembler.toResource(authorizationService.get(authorizationId));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<BlossomAssociationGroupEntityHalResource> create(
			@Validated @RequestBody BlossomAssociationGroupEntityResourceState boScopeResource)
					throws MessagingException {

		BlossomAssociationGroupEntityServiceDTO created = authorizationService
				.create(assembler.fromResourceState(boScopeResource, resourceType));
		BlossomAssociationGroupEntityHalResource resource = assembler.toResource(created);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(entityLinks.linkForSingleResource(resource).toUri());

		return new ResponseEntity<BlossomAssociationGroupEntityHalResource>(resource, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void delete(@PathVariable("id") Long id) {
		authorizationService.delete(id);
	}

	public static TemplateVariables getTemplateVariables() {
		return new TemplateVariables(new TemplateVariable("groupId", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("entityId", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM));
	}

}
