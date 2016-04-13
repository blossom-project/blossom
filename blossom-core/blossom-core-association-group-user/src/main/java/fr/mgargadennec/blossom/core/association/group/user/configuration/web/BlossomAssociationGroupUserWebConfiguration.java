package fr.mgargadennec.blossom.core.association.group.user.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;

import fr.mgargadennec.blossom.core.association.group.user.web.assembler.BlossomAssociationGroupUserResourceAssembler;
import fr.mgargadennec.blossom.core.association.group.user.web.controller.BlossomAssociationGroupUserController;
import fr.mgargadennec.blossom.core.association.group.user.web.resource.BlossomAssociationGroupUserHalResource;
import fr.mgargadennec.blossom.core.association.group.user.web.validator.BlossomAssociationGroupUserValidator;
import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResourceDefinition;
import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.group.web.assembler.BlossomGroupResourceAssembler;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.core.user.web.assembler.BlossomUserResourceAssembler;

@Configuration
@ComponentScan
public class BlossomAssociationGroupUserWebConfiguration {

	@Bean
	BlossomAssociationGroupUserResourceAssembler blossomAssociationGroupUserAssembler(
			BlossomUserResourceAssembler blossomUserResourceAssembler,
			BlossomGroupResourceAssembler blossomGroupResourceAssembler, IBlossomUserService blossomUserService,
			IBlossomGroupService blossomGroupService) {
		return new BlossomAssociationGroupUserResourceAssembler(blossomUserResourceAssembler,
				blossomGroupResourceAssembler, blossomUserService, blossomGroupService);
	}

	@Bean
	BlossomAssociationGroupUserValidator blossomAssociationGroupUserValidator() {
		return new BlossomAssociationGroupUserValidator();
	}

	@Bean
	public BlossomAssociationGroupUserController blossomAssociationGroupUserController() {
		return new BlossomAssociationGroupUserController();
	}

	@Bean
	BlossomRootResourceDefinition blossomAssociationGroupUserRootResourceDefinition(EntityLinks entityLinks,
			RelProvider relProvider) {
		return new BlossomRootResourceDefinition() {

			public boolean supports(Class<? extends Resource> delimiter) {
				return BlossomAssociationGroupUserHalResource.class.isAssignableFrom(delimiter);
			}

			@Override
			public Link getLink() {
				return entityLinks.linkToCollectionResource(BlossomAssociationGroupUserHalResource.class)
						.withRel(relProvider.getCollectionResourceRelFor(BlossomAssociationGroupUserHalResource.class));
			}
		};
	}
}
