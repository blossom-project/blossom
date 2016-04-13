package fr.mgargadennec.blossom.core.user.configuration.web;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;

import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResourceDefinition;
import fr.mgargadennec.blossom.core.common.web.validator.BlossomEmailValidator;
import fr.mgargadennec.blossom.core.common.web.validator.BlossomPhoneValidator;
import fr.mgargadennec.blossom.core.user.web.assembler.BlossomUserResourceAssembler;
import fr.mgargadennec.blossom.core.user.web.controller.BlossomUserController;
import fr.mgargadennec.blossom.core.user.web.resources.BlossomUserHalResource;
import fr.mgargadennec.blossom.core.user.web.validator.BlossomUserPasswordValidator;
import fr.mgargadennec.blossom.core.user.web.validator.BlossomUserValidator;

@Configuration
@ComponentScan
public class BlossomUserWebConfiguration {

	@Bean
	BlossomUserResourceAssembler assembler() {
		return new BlossomUserResourceAssembler();
	}

	@Bean
	BlossomUserValidator userValidator(BlossomPhoneValidator phoneValidator, BlossomEmailValidator emailValidator) {
		return new BlossomUserValidator(phoneValidator, emailValidator);
	}

	@Bean
	BlossomUserPasswordValidator userPasswordValidator() {
		return new BlossomUserPasswordValidator();
	}

	@Bean
	public BlossomUserController blossomUsercontroller() {
		return new BlossomUserController();
	}
	
	@Bean 
	BlossomRootResourceDefinition blossomUserRootResourceDefinition(EntityLinks entityLinks, RelProvider relProvider){
		return new BlossomRootResourceDefinition() {
			
			public boolean supports(Class<? extends Resource> delimiter) {
				return BlossomUserHalResource.class.isAssignableFrom(delimiter);
			}
			
			@Override
			public Link getLink() {
				return entityLinks.linkToCollectionResource(BlossomUserHalResource.class)
						.withRel(relProvider.getCollectionResourceRelFor(BlossomUserHalResource.class));
			}
		};
	}
}
