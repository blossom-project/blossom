package fr.mgargadennec.blossom.core.group.configuration.root.technical;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.plugin.core.PluginRegistry;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexInfosDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexationAssociationDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilderAssociation;
import fr.mgargadennec.blossom.core.group.constants.BlossomGroupConst;
import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.group.service.dto.BlossomGroupServiceDTO;
import fr.mgargadennec.blossom.core.group.support.indexation.builder.impl.BlossomGroupESResourceIndexBuilderImpl;
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.BlossomScheduledResourcesExtractorImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomGroupIndexationConfiguration {

	@Autowired
	private ResourceLoader loader;

	@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_REGISTRY)
	@Autowired
	private PluginRegistry<IBlossomESResourceIndexBuilder, String> esResourceIndexPluginRegistry;

	@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_ASSOCIATIONS_REGISTRY)
	@Autowired
	private PluginRegistry<IBlossomESResourceIndexBuilderAssociation, String> esResourceIndexAssociationPluginRegistry;
	
	@Bean
	public BlossomGroupESResourceIndexBuilderImpl blossomGroupESResourceIndexBuilderImpl() {
		return new BlossomGroupESResourceIndexBuilderImpl();
	}

	@Bean
	@ConditionalOnBean(IBlossomGroupService.class)
	public BlossomScheduledResourcesExtractorImpl<BlossomGroupServiceDTO> blossomGroupsScheduledIndexation(Client client,
			IBlossomGroupService GroupService, 
			IBlossomAuthenticationUtilService blossomAuthenticationUtilService) throws IOException {
		Resource settingsResource = loader.getResource("classpath:/settings/es/groups.json");
		String settings = null;
		if (settingsResource.exists()) {
			settings = Resources.toString(settingsResource.getURL(), Charsets.UTF_8);
		}

		BlossomIndexInfosDTO GroupsIndexInfos = esResourceIndexPluginRegistry
				.getPluginFor(BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME).getIndexInfos();

		List<IBlossomESResourceIndexBuilderAssociation> associationsPlugins = esResourceIndexAssociationPluginRegistry
				.getPluginsFor(BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME);
		List<BlossomIndexationAssociationDTO<? extends BlossomAbstractServiceDTO>> indexationAssociations = associationsPlugins
				.stream().map(p -> p.getAssociationInfos()).collect(Collectors.toList());

		return new BlossomScheduledResourcesExtractorImpl<BlossomGroupServiceDTO>(
				client, 
				GroupsIndexInfos, 
				GroupService,
				BlossomGroupServiceDTO.class, 
				settings, 
				blossomAuthenticationUtilService, 
				indexationAssociations);
	}
}
