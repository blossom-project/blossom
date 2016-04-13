package fr.mgargadennec.blossom.core.right.configuration.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.right.configuration.properties.BlossomRightProperties;
import fr.mgargadennec.blossom.core.right.process.IBlossomRightProcess;
import fr.mgargadennec.blossom.core.right.service.IBlossomRightService;
import fr.mgargadennec.blossom.core.right.service.impl.BlossomRightServiceImpl;

@Configuration
public class BlossomRightServiceConfiguration {

	@Autowired
	private BlossomRightProperties rightProperties;

	@Bean
	public IBlossomRightService blossomRightService(
			IBlossomRightProcess blossomRightProcess,
			ApplicationEventPublisher eventPublisher,
			@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_REGISTRY) PluginRegistry<IBlossomESResourceIndexBuilder, String> esResourceIndexBuilderRegistry) {

		return new BlossomRightServiceImpl(blossomRightProcess, eventPublisher);
	}

}
