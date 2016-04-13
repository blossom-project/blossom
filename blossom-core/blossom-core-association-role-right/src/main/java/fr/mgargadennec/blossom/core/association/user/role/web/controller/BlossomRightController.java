package fr.mgargadennec.blossom.core.association.user.role.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.ExposesResourceFor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.PagedResources;
import org.springframework.hateoas.RelProvider;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.mgargadennec.blossom.core.association.user.role.web.assembler.BlossomRightResourceAssembler;
import fr.mgargadennec.blossom.core.association.user.role.web.resource.BlossomRightHalResource;
import fr.mgargadennec.blossom.core.association.user.role.web.resource.BlossomRightResourceState;
import fr.mgargadennec.blossom.core.association.user.role.web.validator.BlossomRightValidator;
import fr.mgargadennec.blossom.core.right.service.IBlossomRightService;
import fr.mgargadennec.blossom.core.right.service.dto.BlossomRightServiceDTO;
import fr.mgargadennec.blossom.core.role.web.controller.BlossomRoleController;

@Controller
@RequestMapping("/roles/{roleId}/rights")
@ExposesResourceFor(BlossomRightHalResource.class)
public class BlossomRightController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BlossomRightController.class);

	@Autowired
	IBlossomRightService rightService;

	@Autowired
	EntityLinks entityLinks;

	@Autowired
	RelProvider relProvider;

	@Autowired
	BlossomRightResourceAssembler assembler;

	@Autowired
	BlossomRightValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody PagedResources<BlossomRightHalResource> rights(@PathVariable("roleId") Long roleId,
			@PageableDefault Pageable pageable,
			PagedResourcesAssembler<BlossomRightServiceDTO> pagedResourcesAssembler) {
		PagedResources<BlossomRightHalResource> pagedResources = pagedResourcesAssembler
				.toResource(rightService.getByRoleId(pageable, roleId), assembler);

		UriTemplate uriTemplate = new UriTemplate(
				entityLinks.linkFor(BlossomRightHalResource.class, roleId).toUri().toString() + "/{id}");
		pagedResources.add(new Link(uriTemplate, "right"));

		Link link = linkTo(methodOn(BlossomRightController.class).rights(roleId, null, null)).withRel(
				"paged" + WordUtils.capitalize(relProvider.getCollectionResourceRelFor(BlossomRightHalResource.class)));
		pagedResources.add(pagedResourcesAssembler.appendPaginationParameterTemplates(link));

		return pagedResources;
	}

	@RequestMapping(value = "/{rightId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BlossomRightHalResource get(@PathVariable("rightId") Long rightId, @PathVariable("roleId") Long roleId) {
		BlossomRightServiceDTO serviceDTO = rightService.get(rightId);
		BlossomRightHalResource resource = assembler.toResource(serviceDTO);

		if (!serviceDTO.getRoleId().equals(roleId)) {
			LOGGER.error("Le droit " + rightId + "n'est pas associ� au role" + roleId);
			throw new IllegalArgumentException("Le droit " + rightId + "n'est pas associ� au role" + roleId);
		}

		resource.add(new Link(new UriTemplate(linkTo(methodOn(BlossomRoleController.class).get(roleId))
				.toUriComponentsBuilder().build().toUriString()), "role"));

		return resource;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<BlossomRightHalResource> create(
			@Validated @RequestBody BlossomRightResourceState boRightResourceState) {
		BlossomRightServiceDTO createdRight = rightService.create(assembler.fromResourceState(boRightResourceState));
		BlossomRightHalResource resource = assembler.toResource(createdRight);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(entityLinks.linkFor(BlossomRightHalResource.class, resource, resource.getId()).toUri());
		return new ResponseEntity<BlossomRightHalResource>(resource, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{rightId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void update(@PathVariable("rightId") Long rightId, @PathVariable("roleId") Long roleId,
			@Validated @RequestBody BlossomRightResourceState boRightResourceState) {
		BlossomRightServiceDTO boRightServiceDTO = assembler.fromResourceState(boRightResourceState);

		if (!boRightServiceDTO.getRoleId().equals(roleId)) {
			LOGGER.error("Le droit " + rightId + " n'est pas associ� au role " + roleId);
			throw new IllegalArgumentException("Le droit " + rightId + " n'est pas associ� au role" + roleId);
		}

		rightService.update(rightId, boRightServiceDTO);
	}

	@RequestMapping(value = "/{rightId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void delete(@PathVariable("rightId") Long rightId, @PathVariable("roleId") Long roleId) {
		BlossomRightServiceDTO boRightServiceDTO = rightService.get(rightId);

		if (!boRightServiceDTO.getRoleId().equals(roleId)) {
			LOGGER.error("Le droit " + rightId + "n'est pas associ� au role" + roleId);
			throw new IllegalArgumentException("Le droit " + rightId + "n'est pas associ� au role" + roleId);
		}

		rightService.delete(rightId);
	}

}
