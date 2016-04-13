package fr.mgargadennec.blossom.core.association.user.role.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;

import fr.mgargadennec.blossom.core.association.user.role.web.assembler.BlossomRightResourceAssembler;
import fr.mgargadennec.blossom.core.association.user.role.web.controller.BlossomRightController;
import fr.mgargadennec.blossom.core.association.user.role.web.resource.BlossomRightHalResource;
import fr.mgargadennec.blossom.core.association.user.role.web.validator.BlossomRightValidator;
import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResourceDefinition;

@Configuration
@ComponentScan
public class BlossomAssociationRoleRightWebConfiguration {

	@Bean
	BlossomRightResourceAssembler blossomRightResourceAssembler() {
		return new BlossomRightResourceAssembler();
	}

	@Bean
	BlossomRightValidator blossomRightValidator() {
		return new BlossomRightValidator();
	}

	@Bean
	public BlossomRightController blossomRightController() {
		return new BlossomRightController();
	}
}
