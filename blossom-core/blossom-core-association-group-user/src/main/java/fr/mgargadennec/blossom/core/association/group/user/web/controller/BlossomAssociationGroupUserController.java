package fr.mgargadennec.blossom.core.association.group.user.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.mgargadennec.blossom.core.association.group.user.service.IBlossomAssociationGroupUserService;
import fr.mgargadennec.blossom.core.association.group.user.service.dto.BlossomAssociationGroupUserServiceDTO;
import fr.mgargadennec.blossom.core.association.group.user.web.assembler.BlossomAssociationGroupUserResourceAssembler;
import fr.mgargadennec.blossom.core.association.group.user.web.resource.BlossomAssociationGroupUserHalResource;
import fr.mgargadennec.blossom.core.association.group.user.web.resource.BlossomAssociationGroupUserResourceState;
import fr.mgargadennec.blossom.core.association.group.user.web.validator.BlossomAssociationGroupUserValidator;
import fr.mgargadennec.blossom.core.group.web.controller.BlossomGroupController;
import fr.mgargadennec.blossom.core.group.web.resource.BlossomGroupHalResource;
import fr.mgargadennec.blossom.core.user.web.controller.BlossomUserController;
import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserHalResource;

@Controller
@RequestMapping("/memberships")
@ExposesResourceFor(BlossomAssociationGroupUserHalResource.class)
public class BlossomAssociationGroupUserController {

	@Autowired
	IBlossomAssociationGroupUserService associationGroupUserService;

	@Autowired
	EntityLinks entityLinks;

	@Autowired
	RelProvider relProvider;

	@Autowired
	BlossomAssociationGroupUserResourceAssembler assembler;

	@Autowired
	BlossomAssociationGroupUserValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody PagedResources<BlossomAssociationGroupUserHalResource> associationsGroupUser(
			@PageableDefault Pageable pageable, @RequestParam(value = "userId", required = false) Long userId,
			@RequestParam(value = "groupId", required = false) Long groupId,
			PagedResourcesAssembler<BlossomAssociationGroupUserServiceDTO> pagedResourcesAssembler) {

		Link link = linkTo(methodOn(BlossomAssociationGroupUserController.class).associationsGroupUser(pageable, userId,
				groupId, pagedResourcesAssembler)).withSelfRel();

		Page<BlossomAssociationGroupUserServiceDTO> page = associationGroupUserService.getAll(pageable, userId,
				groupId);

		PagedResources<BlossomAssociationGroupUserHalResource> pagedResources = pagedResourcesAssembler.toResource(page,
				assembler, link);

		UriTemplate uriTemplate = new UriTemplate(
				entityLinks.linkFor(BlossomAssociationGroupUserHalResource.class).toUri().toString() + "/{id}");
		pagedResources.add(new Link(uriTemplate, "membership"));

		UriTemplate searchUri = new UriTemplate(linkTo(
				methodOn(BlossomAssociationGroupUserController.class).associationsGroupUser(null, null, null, null))
						.toUriComponentsBuilder().build().toUriString(),
				getTemplateVariables());
		pagedResources.add(new Link(searchUri,
				relProvider.getCollectionResourceRelFor(BlossomAssociationGroupUserHalResource.class)));

		UriTemplate groupsUri = new UriTemplate(linkTo(methodOn(BlossomGroupController.class).groups(null, null, null))
				.toUriComponentsBuilder().build().toUriString(), BlossomGroupController.getTemplateVariables());
		pagedResources.add(new Link(groupsUri, relProvider.getCollectionResourceRelFor(BlossomGroupHalResource.class)));

		UriTemplate usersUri = new UriTemplate(linkTo(methodOn(BlossomUserController.class).users(null, null, null))
				.toUriComponentsBuilder().build().toUriString(), BlossomUserController.getTemplateVariables());
		pagedResources.add(new Link(usersUri, relProvider.getCollectionResourceRelFor(BlossomUserHalResource.class)));

		return pagedResources;
	}

	@RequestMapping(value = "/{membershipId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BlossomAssociationGroupUserHalResource get(@PathVariable("membershipId") Long membershipId) {
		return assembler.toResource(associationGroupUserService.get(membershipId));
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<BlossomAssociationGroupUserHalResource> create(
			@Validated @RequestBody BlossomAssociationGroupUserResourceState boAssociationGroupUserResource) {
		BlossomAssociationGroupUserServiceDTO createdAssociationGroupUser = associationGroupUserService
				.create(assembler.fromResourceState(boAssociationGroupUserResource));
		BlossomAssociationGroupUserHalResource resource = assembler.toResource(createdAssociationGroupUser);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(entityLinks.linkForSingleResource(resource).toUri());
		return new ResponseEntity<BlossomAssociationGroupUserHalResource>(resource, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{membershipId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void update(@PathVariable("membershipId") Long membershipId,
			@RequestBody BlossomAssociationGroupUserResourceState boMembershipResource) {
		BlossomAssociationGroupUserServiceDTO boAssociationGroupUserServiceDTO = assembler
				.fromResourceState(boMembershipResource);
		associationGroupUserService.update(membershipId, boAssociationGroupUserServiceDTO);
	}

	@RequestMapping(value = "/{membershipId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void delete(@PathVariable("membershipId") Long membershipId) {
		associationGroupUserService.delete(membershipId);
	}

	public static TemplateVariables getTemplateVariables() {
		return new TemplateVariables(new TemplateVariable("groupId", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("userId", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM));
	}

}
