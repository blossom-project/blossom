package fr.mgargadennec.blossom.core.group.configuration.root.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.common.support.mail.IBlossomMailService;
import fr.mgargadennec.blossom.core.group.configuration.root.properties.BlossomGroupProperties;
import fr.mgargadennec.blossom.core.group.constants.BlossomGroupConst;
import fr.mgargadennec.blossom.core.group.process.IBlossomGroupProcess;
import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.group.service.impl.BlossomGroupServiceImpl;
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.IBlossomElasticsearchQueryService;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomGroupServiceConfiguration {

	@Autowired
	private BlossomGroupProperties blossomGroupProperties;

	@Bean
	public IBlossomGroupService blossomGroupService(IBlossomGroupProcess blossomGroupProcess,
			IBlossomElasticsearchQueryService esService, 
			IBlossomMailService blossomMailService, 
			IBlossomAuthenticationUtilService boAuthenticationUtilService, 
			ApplicationEventPublisher eventPublisher,
			@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_REGISTRY) PluginRegistry<IBlossomESResourceIndexBuilder, String> esResourceIndexBuilderRegistry) {

		return new BlossomGroupServiceImpl(blossomGroupProcess, esService, boAuthenticationUtilService, eventPublisher,
				esResourceIndexBuilderRegistry.getPluginFor(BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME));
	}
}
