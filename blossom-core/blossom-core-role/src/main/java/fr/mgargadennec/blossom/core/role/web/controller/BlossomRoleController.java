package fr.mgargadennec.blossom.core.role.web.controller;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import javax.mail.MessagingException;

import org.apache.commons.lang3.text.WordUtils;
import org.elasticsearch.client.Client;
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

import fr.mgargadennec.blossom.core.role.service.IBlossomRoleService;
import fr.mgargadennec.blossom.core.role.service.dto.BlossomRoleServiceDTO;
import fr.mgargadennec.blossom.core.role.web.assembler.BlossomRoleResourceAssembler;
import fr.mgargadennec.blossom.core.role.web.resource.BlossomRoleHalResource;
import fr.mgargadennec.blossom.core.role.web.resource.BlossomRoleResourceState;
import fr.mgargadennec.blossom.core.role.web.validator.BlossomRoleValidator;

@Controller
@RequestMapping("/roles")
@ExposesResourceFor(BlossomRoleHalResource.class)
public class BlossomRoleController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BlossomRoleController.class);

	@Autowired
	IBlossomRoleService roleService;

	@Autowired
	EntityLinks entityLinks;

	@Autowired
	RelProvider relProvider;

	@Autowired
	BlossomRoleResourceAssembler assembler;

	@Autowired
	BlossomRoleValidator roleValidator;

	@Autowired
	Client client;

	@InitBinder(value = "BlossomRoleResourceState")
	protected void initBinderUser(WebDataBinder binder) {
		binder.setValidator(roleValidator);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody PagedResources<BlossomRoleHalResource> roles(@PageableDefault(sort = "name") Pageable pageable,
			@RequestParam(value = "q", defaultValue = "", required = false) String q,
			PagedResourcesAssembler<BlossomRoleServiceDTO> pagedResourcesAssembler) {

		Link link = linkTo(methodOn(BlossomRoleController.class).roles(pageable, q, pagedResourcesAssembler))
				.withSelfRel();

		Page<BlossomRoleServiceDTO> currentPage = null;
		if (!Strings.isNullOrEmpty(q)) {
			currentPage = roleService.search(q, pageable);
		} else {
			currentPage = roleService.getAll(pageable);
		}

		PagedResources<BlossomRoleHalResource> pagedResources = pagedResourcesAssembler.toResource(currentPage,
				assembler, link);

		UriTemplate roleUritemplate = new UriTemplate(
				entityLinks.linkFor(BlossomRoleHalResource.class).toUri().toString() + "/{id}");
		pagedResources.add(new Link(roleUritemplate, relProvider.getItemResourceRelFor(BlossomRoleHalResource.class)));

		UriTemplate searchUri = new UriTemplate(linkTo(methodOn(BlossomRoleController.class).roles(null, null, null))
				.toUriComponentsBuilder().build().toUriString(), getTemplateVariables());
		pagedResources.add(new Link(searchUri,
				"paged" + WordUtils.capitalize(relProvider.getCollectionResourceRelFor(BlossomRoleHalResource.class))));

		return pagedResources;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BlossomRoleHalResource get(@PathVariable("id") Long id) {
		BlossomRoleHalResource resource = assembler.toResource(roleService.get(id));
		return resource;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<BlossomRoleHalResource> create(
			@Validated @RequestBody BlossomRoleResourceState BlossomRoleResourceState) throws MessagingException {
		BlossomRoleServiceDTO created = roleService.create(assembler.fromResourceState(BlossomRoleResourceState));
		BlossomRoleHalResource resource = assembler.toResource(created);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(entityLinks.linkForSingleResource(resource).toUri());

		return new ResponseEntity<BlossomRoleHalResource>(resource, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void update(@PathVariable("id") Long id,
			@Validated @RequestBody BlossomRoleResourceState BlossomRoleResourceState) {
		BlossomRoleServiceDTO BlossomRoleServiceDTO = assembler.fromResourceState(BlossomRoleResourceState);
		roleService.update(id, BlossomRoleServiceDTO);
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void delete(@PathVariable("id") Long id) {
		roleService.delete(id);
	}

	public static TemplateVariables getTemplateVariables() {
		return new TemplateVariables(new TemplateVariable("q", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM));
	}
}
