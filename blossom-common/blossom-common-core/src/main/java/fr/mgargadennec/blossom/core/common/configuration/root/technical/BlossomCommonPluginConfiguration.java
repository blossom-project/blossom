package fr.mgargadennec.blossom.core.common.configuration.root.technical;

import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.config.EnablePluginRegistries;

import fr.mgargadennec.blossom.core.common.service.IBlossomServicePlugin;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilderAssociation;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomScheduledResourcesExtractor;

@Configuration
@EnablePluginRegistries(value = { 
		IBlossomServicePlugin.class,
		IBlossomEntityDefinition.class,
		IBlossomScheduledResourcesExtractor.class, 
		IBlossomESResourceIndexBuilder.class,
		IBlossomESResourceIndexBuilderAssociation.class,
		IBlossomHistoryBuilder.class })
public class BlossomCommonPluginConfiguration {

}
