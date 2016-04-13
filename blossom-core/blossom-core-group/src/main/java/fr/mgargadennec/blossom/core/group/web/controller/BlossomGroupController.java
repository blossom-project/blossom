package fr.mgargadennec.blossom.core.group.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.apache.commons.lang3.text.WordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

import com.google.common.base.Strings;

import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.group.service.dto.BlossomGroupServiceDTO;
import fr.mgargadennec.blossom.core.group.web.assembler.BlossomGroupResourceAssembler;
import fr.mgargadennec.blossom.core.group.web.resource.BlossomGroupHalResource;
import fr.mgargadennec.blossom.core.group.web.resource.BlossomGroupResourceState;
import fr.mgargadennec.blossom.core.group.web.validator.BlossomGroupValidator;

@Controller
@RequestMapping("/groups")
@ExposesResourceFor(BlossomGroupHalResource.class)
public class BlossomGroupController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BlossomGroupController.class);

	@Autowired
	IBlossomGroupService groupService;

	@Autowired
	EntityLinks entityLinks;

	@Autowired
	RelProvider relProvider;

	@Autowired
	BlossomGroupResourceAssembler assembler;

	@Autowired
	BlossomGroupValidator validator;

	@InitBinder
	protected void initBinder(WebDataBinder binder) {
		binder.setValidator(validator);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody PagedResources<BlossomGroupHalResource> groups(
			@PageableDefault(sort = "name") Pageable pageable,
			@RequestParam(value = "q", required = false, defaultValue = "") String q,
			PagedResourcesAssembler<BlossomGroupServiceDTO> pagedResourcesAssembler) {

		Link link = linkTo(methodOn(BlossomGroupController.class).groups(pageable, q, pagedResourcesAssembler))
				.withSelfRel();

		Page<BlossomGroupServiceDTO> currentPage = null;
		if (!Strings.isNullOrEmpty(q)) {
			currentPage = groupService.search(q, pageable);
		} else {
			currentPage = groupService.getAll(pageable);
		}

		PagedResources<BlossomGroupHalResource> pagedResources = pagedResourcesAssembler.toResource(currentPage,
				assembler, link);

		UriTemplate userUritemplate = new UriTemplate(
				entityLinks.linkFor(BlossomGroupHalResource.class).toUri().toString() + "/{id}");
		pagedResources.add(new Link(userUritemplate, relProvider.getItemResourceRelFor(BlossomGroupHalResource.class)));

		UriTemplate searchUri = new UriTemplate(
				linkTo(methodOn(BlossomGroupController.class).groups(null, null, null)).toUriComponentsBuilder()
						.build().toUriString(),
				getTemplateVariables());
		pagedResources.add(new Link(searchUri, relProvider.getCollectionResourceRelFor(BlossomGroupHalResource.class)));

		return pagedResources;
	}

	@RequestMapping(value = "/{groupId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BlossomGroupHalResource get(@PathVariable("groupId") Long groupId) {
		BlossomGroupHalResource resource = assembler.toResource(groupService.get(groupId));

		return resource;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<BlossomGroupHalResource> create(
			@Validated @RequestBody BlossomGroupResourceState boGroupResource) {
		BlossomGroupServiceDTO createdGroup = groupService.create(assembler.fromResourceState(boGroupResource));
		BlossomGroupHalResource resource = assembler.toResource(createdGroup);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(entityLinks.linkForSingleResource(resource).toUri());
		return new ResponseEntity<BlossomGroupHalResource>(resource, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{groupId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void update(@PathVariable("groupId") Long groupId,
			@Validated @RequestBody BlossomGroupResourceState boGroupResource) {
		BlossomGroupServiceDTO groupServiceDTO = assembler.fromResourceState(boGroupResource);
		groupService.update(groupId, groupServiceDTO);
	}

	@RequestMapping(value = "/{groupId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void delete(@PathVariable("groupId") Long groupId) {
		groupService.delete(groupId);
	}

	public static TemplateVariables getTemplateVariables() {
		return new TemplateVariables(new TemplateVariable("q", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM));
	}
}
