package fr.mgargadennec.blossom.core.role.configuration.root.technical;

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
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.BlossomScheduledResourcesExtractorImpl;
import fr.mgargadennec.blossom.core.role.constants.BlossomRoleConst;
import fr.mgargadennec.blossom.core.role.service.IBlossomRoleService;
import fr.mgargadennec.blossom.core.role.service.dto.BlossomRoleServiceDTO;
import fr.mgargadennec.blossom.core.role.support.indexation.builder.impl.BlossomRoleESResourceIndexBuilderImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomRoleIndexationConfiguration {

	@Autowired
	private ResourceLoader loader;

	@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_REGISTRY)
	@Autowired
	private PluginRegistry<IBlossomESResourceIndexBuilder, String> esResourceIndexPluginRegistry;

	@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_ASSOCIATIONS_REGISTRY)
	@Autowired
	private PluginRegistry<IBlossomESResourceIndexBuilderAssociation, String> esResourceIndexAssociationPluginRegistry;
	
	@Bean
	public BlossomRoleESResourceIndexBuilderImpl blossomRoleESResourceIndexBuilderImpl() {
		return new BlossomRoleESResourceIndexBuilderImpl();
	}

	@Bean
	@ConditionalOnBean(IBlossomRoleService.class)
	public BlossomScheduledResourcesExtractorImpl<BlossomRoleServiceDTO> blossomRolesScheduledIndexation(Client client,
			IBlossomRoleService userService, 
			IBlossomAuthenticationUtilService blossomAuthenticationUtilService) throws IOException {
		Resource settingsResource = loader.getResource("classpath:/settings/es/roles.json");
		String settings = null;
		if (settingsResource.exists()) {
			settings = Resources.toString(settingsResource.getURL(), Charsets.UTF_8);
		}

		BlossomIndexInfosDTO usersIndexInfos = esResourceIndexPluginRegistry
				.getPluginFor(BlossomRoleConst.BLOSSOM_ROLE_ENTITY_NAME).getIndexInfos();

		List<IBlossomESResourceIndexBuilderAssociation> associationsPlugins = esResourceIndexAssociationPluginRegistry
				.getPluginsFor(BlossomRoleConst.BLOSSOM_ROLE_ENTITY_NAME);
		List<BlossomIndexationAssociationDTO<? extends BlossomAbstractServiceDTO>> indexationAssociations = associationsPlugins
				.stream().map(p -> p.getAssociationInfos()).collect(Collectors.toList());

		return new BlossomScheduledResourcesExtractorImpl<BlossomRoleServiceDTO>(
				client, 
				usersIndexInfos, 
				userService,
				BlossomRoleServiceDTO.class, 
				settings, 
				blossomAuthenticationUtilService, 
				indexationAssociations);
	}
}
