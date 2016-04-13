package fr.mgargadennec.blossom.core.user.web.controller;

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
import org.springframework.mail.MailSendException;
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

import fr.mgargadennec.blossom.core.user.model.BlossomUserStateEnum;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.core.user.service.dto.BlossomUserServiceDTO;
import fr.mgargadennec.blossom.core.user.web.assembler.BlossomUserResourceAssembler;
import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserHalResource;
import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserPasswordResource;
import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserResourceState;
import fr.mgargadennec.blossom.core.user.web.validator.BlossomUserPasswordValidator;
import fr.mgargadennec.blossom.core.user.web.validator.BlossomUserValidator;

@Controller
@RequestMapping("/users")
@ExposesResourceFor(BlossomUserHalResource.class)
public class BlossomUserController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BlossomUserController.class);

	@Autowired
	IBlossomUserService userService;

	@Autowired
	EntityLinks entityLinks;

	@Autowired
	RelProvider relProvider;

	@Autowired
	BlossomUserResourceAssembler assembler;

	@Autowired
	BlossomUserValidator userValidator;

	@Autowired
	BlossomUserPasswordValidator userPasswordValidator;

	@InitBinder(value = "boUserResourceState")
	protected void initBinderUser(WebDataBinder binder) {
		binder.setValidator(userValidator);
	}

	@InitBinder(value = "boUserPasswordResource")
	protected void initBinderUserPassword(WebDataBinder binder) {
		binder.setValidator(userPasswordValidator);
	}

	@RequestMapping(method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody PagedResources<BlossomUserHalResource> users(
			@PageableDefault(sort = "lastname") Pageable pageable,
			@RequestParam(value = "q", defaultValue = "", required = false) String q,
			PagedResourcesAssembler<BlossomUserServiceDTO> pagedResourcesAssembler) {

		Link link = linkTo(
				methodOn(BlossomUserController.class).users(pageable, q, pagedResourcesAssembler))
						.withSelfRel();

		Page<BlossomUserServiceDTO> currentPage = null;
		if (!Strings.isNullOrEmpty(q)) {
			currentPage = userService.search(q, pageable);
		} else {
			currentPage = userService.getAll(pageable);
		}

		PagedResources<BlossomUserHalResource> pagedResources = pagedResourcesAssembler.toResource(currentPage,
				assembler, link);

		UriTemplate userUritemplate = new UriTemplate(
				entityLinks.linkFor(BlossomUserHalResource.class).toUri().toString() + "/{id}");
		pagedResources.add(new Link(userUritemplate, relProvider.getItemResourceRelFor(BlossomUserHalResource.class)));

		UriTemplate searchUri = new UriTemplate(
				linkTo(methodOn(BlossomUserController.class).users(null, null, null))
						.toUriComponentsBuilder().build().toUriString(),
				getTemplateVariables());
		pagedResources.add(new Link(searchUri,
				"paged" + WordUtils.capitalize(relProvider.getCollectionResourceRelFor(BlossomUserHalResource.class))));

		return pagedResources;
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public BlossomUserHalResource get(@PathVariable("userId") Long userId) {
		BlossomUserHalResource resource = assembler.toResource(userService.get(userId));
		return resource;
	}

	@RequestMapping(method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.CREATED)
	public ResponseEntity<BlossomUserHalResource> create(
			@Validated @RequestBody BlossomUserResourceState boUserResourceState) {

		BlossomUserServiceDTO createdUser = userService.create(assembler.fromResourceState(boUserResourceState));
		BlossomUserHalResource resource = assembler.toResource(createdUser);

		HttpHeaders headers = new HttpHeaders();
		headers.setLocation(entityLinks.linkForSingleResource(resource).toUri());

		return new ResponseEntity<BlossomUserHalResource>(resource, headers, HttpStatus.CREATED);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.PUT)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void update(@PathVariable("userId") Long userId,
			@Validated @RequestBody BlossomUserResourceState boUserResourceState) {
		BlossomUserServiceDTO BlossomUserServiceDTO = assembler.fromResourceState(boUserResourceState);
		userService.update(userId, BlossomUserServiceDTO);
	}

	@RequestMapping(value = "/{userId}", method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	@ResponseBody
	public void delete(@PathVariable("userId") Long userId) {
		userService.delete(userId);
	}

	public static TemplateVariables getTemplateVariables() {
		return new TemplateVariables(new TemplateVariable("q", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("page", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("sort", TemplateVariable.VariableType.REQUEST_PARAM),
				new TemplateVariable("size", TemplateVariable.VariableType.REQUEST_PARAM));
	}
}
