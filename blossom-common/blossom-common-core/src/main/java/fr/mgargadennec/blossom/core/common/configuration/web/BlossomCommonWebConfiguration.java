package fr.mgargadennec.blossom.core.common.configuration.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.UriTemplate;
import org.springframework.hateoas.hal.CurieProvider;
import org.springframework.hateoas.hal.DefaultCurieProvider;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;

import fr.mgargadennec.blossom.core.common.configuration.properties.BlossomCommonProperties;
import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.web.controller.BlossomApiController;
import fr.mgargadennec.blossom.core.common.web.resources.BlossomRootResourceDefinition;
import fr.mgargadennec.blossom.core.common.web.validator.BlossomEmailValidator;
import fr.mgargadennec.blossom.core.common.web.validator.BlossomPhoneValidator;

@Configuration
@EnablePluginRegistries(BlossomRootResourceDefinition.class)
public class BlossomCommonWebConfiguration {
	
	@Autowired
	private BlossomCommonProperties properties;

	@Autowired
	@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_ROOT_RESOURCE_REGISTRY)
	public PluginRegistry<BlossomRootResourceDefinition, Class<? extends Resource>> resourcesRegistry;

	@Bean
	public CurieProvider curieProvider() {
		return new DefaultCurieProvider(properties.getHateoas().getCurie().getPrefix(), new UriTemplate(properties.getHateoas().getCurie().getUri()));
	}

	@Bean
	BlossomPhoneValidator blossomPhoneValidator() {
		return new BlossomPhoneValidator();
	}

	@Bean
	BlossomEmailValidator blossomEmailValidator() {
		return new BlossomEmailValidator();
	}

	@Bean
	BlossomApiController blossomApiController() {
		return new BlossomApiController(resourcesRegistry);
	}
}
