package fr.mgargadennec.blossom.core.group.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;

import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResourceDefinition;
import fr.mgargadennec.blossom.core.group.web.assembler.BlossomGroupResourceAssembler;
import fr.mgargadennec.blossom.core.group.web.controller.BlossomGroupController;
import fr.mgargadennec.blossom.core.group.web.resource.BlossomGroupHalResource;
import fr.mgargadennec.blossom.core.group.web.validator.BlossomGroupValidator;

@Configuration
@ComponentScan
public class BlossomGroupWebConfiguration {

	@Bean
	BlossomGroupResourceAssembler blossomGroupAssembler() {
		return new BlossomGroupResourceAssembler();
	}

	@Bean
	BlossomGroupValidator blossomGroupValidator() {
		return new BlossomGroupValidator();
	}

	@Bean
	public BlossomGroupController blossomGroupController() {
		return new BlossomGroupController();
	}

	@Bean 
	BlossomRootResourceDefinition blossomGroupRootResourceDefinition(EntityLinks entityLinks, RelProvider relProvider){
		return new BlossomRootResourceDefinition() {
			
			public boolean supports(Class<? extends Resource> delimiter) {
				return BlossomGroupHalResource.class.isAssignableFrom(delimiter);
			}
			
			@Override
			public Link getLink() {
				return entityLinks.linkToCollectionResource(BlossomGroupHalResource.class)
						.withRel(relProvider.getCollectionResourceRelFor(BlossomGroupHalResource.class));
			}
		};
	}
}
