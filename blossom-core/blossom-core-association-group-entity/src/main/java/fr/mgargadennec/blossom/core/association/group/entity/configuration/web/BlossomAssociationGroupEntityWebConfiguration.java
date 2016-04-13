package fr.mgargadennec.blossom.core.association.group.entity.configuration.web;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.EntityLinks;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RelProvider;
import org.springframework.hateoas.Resource;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;

import fr.mgargadennec.blossom.core.association.group.entity.constants.BlossomAssociationGroupEntityConst;
import fr.mgargadennec.blossom.core.association.group.entity.web.controller.BlossomAssociationGroupEntityRootController;
import fr.mgargadennec.blossom.core.association.group.entity.web.resource.BlossomAssociationGroupEntityHalResource;
import fr.mgargadennec.blossom.core.association.group.entity.web.resource.BlossomAssociationGroupEntityResourceDefinition;
import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResourceDefinition;

@Configuration
@ComponentScan
@EnablePluginRegistries(BlossomAssociationGroupEntityResourceDefinition.class)
public class BlossomAssociationGroupEntityWebConfiguration {

	@Qualifier(BlossomAssociationGroupEntityConst.PLUGIN_QUALIFIER_BLOSSOM_ASSOCIATION_GROUP_ENTITY_RESOURCE_REGISTRY)
	@Autowired
	PluginRegistry<BlossomAssociationGroupEntityResourceDefinition, Class<? extends Resource>> registry;

	@Bean
	public BlossomAssociationGroupEntityRootController blossomAuthorizationRootController() {
		return new BlossomAssociationGroupEntityRootController(registry);
	}

	@Bean
	BlossomRootResourceDefinition blossomAssociationGroupEntityRootResourceDefinition(EntityLinks entityLinks,
			RelProvider relProvider) {
		return new BlossomRootResourceDefinition() {

			public boolean supports(Class<? extends Resource> delimiter) {
				return BlossomAssociationGroupEntityHalResource.class.isAssignableFrom(delimiter);
			}

			@Override
			public Link getLink() {
				return linkTo(methodOn(BlossomAssociationGroupEntityRootController.class).apiRoot()).withRel(
						relProvider.getCollectionResourceRelFor(BlossomAssociationGroupEntityHalResource.class));
			}
		};
	}
}
