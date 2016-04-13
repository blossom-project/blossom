package fr.mgargadennec.blossom.core.user.configuration.root.technical;

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
import fr.mgargadennec.blossom.core.user.constants.BlossomUserConst;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.core.user.service.dto.BlossomUserServiceDTO;
import fr.mgargadennec.blossom.core.user.support.indexation.builder.impl.BlossomUserESResourceIndexBuilderImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomUserIndexationConfiguration {

	@Autowired
	private ResourceLoader loader;

	@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_REGISTRY)
	@Autowired
	private PluginRegistry<IBlossomESResourceIndexBuilder, String> esResourceIndexPluginRegistry;

	@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_ASSOCIATIONS_REGISTRY)
	@Autowired
	private PluginRegistry<IBlossomESResourceIndexBuilderAssociation, String> esResourceIndexAssociationPluginRegistry;
	
	@Bean
	public BlossomUserESResourceIndexBuilderImpl blossomUserESResourceIndexBuilderImpl() {
		return new BlossomUserESResourceIndexBuilderImpl();
	}

	@Bean
	@ConditionalOnBean(IBlossomUserService.class)
	public BlossomScheduledResourcesExtractorImpl<BlossomUserServiceDTO> blossomUsersScheduledIndexation(Client client,
			IBlossomUserService userService, 
			IBlossomAuthenticationUtilService blossomAuthenticationUtilService) throws IOException {
		Resource settingsResource = loader.getResource("classpath:/settings/es/users.json");
		String settings = null;
		if (settingsResource.exists()) {
			settings = Resources.toString(settingsResource.getURL(), Charsets.UTF_8);
		}

		BlossomIndexInfosDTO usersIndexInfos = esResourceIndexPluginRegistry
				.getPluginFor(BlossomUserConst.BLOSSOM_USER_ENTITY_NAME).getIndexInfos();

		List<IBlossomESResourceIndexBuilderAssociation> associationsPlugins = esResourceIndexAssociationPluginRegistry
				.getPluginsFor(BlossomUserConst.BLOSSOM_USER_ENTITY_NAME);
		List<BlossomIndexationAssociationDTO<? extends BlossomAbstractServiceDTO>> indexationAssociations = associationsPlugins
				.stream().map(p -> p.getAssociationInfos()).collect(Collectors.toList());

		return new BlossomScheduledResourcesExtractorImpl<BlossomUserServiceDTO>(
				client, 
				usersIndexInfos, 
				userService,
				BlossomUserServiceDTO.class, 
				settings, 
				blossomAuthenticationUtilService, 
				indexationAssociations);
	}
}
