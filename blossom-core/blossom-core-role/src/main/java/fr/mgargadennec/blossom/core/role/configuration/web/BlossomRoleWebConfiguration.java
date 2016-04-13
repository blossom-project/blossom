package fr.mgargadennec.blossom.core.role.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;

import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResourceDefinition;
import fr.mgargadennec.blossom.core.role.web.assembler.BlossomRoleResourceAssembler;
import fr.mgargadennec.blossom.core.role.web.controller.BlossomRoleController;
import fr.mgargadennec.blossom.core.role.web.resource.BlossomRoleHalResource;
import fr.mgargadennec.blossom.core.role.web.validator.BlossomRoleValidator;

@Configuration
@ComponentScan
public class BlossomRoleWebConfiguration {

	@Bean
	BlossomRoleResourceAssembler blossomRoleAssembler() {
		return new BlossomRoleResourceAssembler();
	}

	@Bean
	BlossomRoleValidator blossomRoleValidator() {
		return new BlossomRoleValidator();
	}

	@Bean
	public BlossomRoleController blossomRoleController() {
		return new BlossomRoleController();
	}
	
	@Bean 
	BlossomRootResourceDefinition blossomRoleRootResourceDefinition(EntityLinks entityLinks, RelProvider relProvider){
		return new BlossomRootResourceDefinition() {
			
			public boolean supports(Class<? extends Resource> delimiter) {
				return BlossomRoleHalResource.class.isAssignableFrom(delimiter);
			}
			
			@Override
			public Link getLink() {
				return entityLinks.linkToCollectionResource(BlossomRoleHalResource.class)
						.withRel(relProvider.getCollectionResourceRelFor(BlossomRoleHalResource.class));
			}
		};
	}
}
