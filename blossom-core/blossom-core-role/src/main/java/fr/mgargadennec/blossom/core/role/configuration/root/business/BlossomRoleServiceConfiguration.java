package fr.mgargadennec.blossom.core.role.configuration.root.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.IBlossomElasticsearchQueryService;
import fr.mgargadennec.blossom.core.role.configuration.root.properties.BlossomRoleProperties;
import fr.mgargadennec.blossom.core.role.constants.BlossomRoleConst;
import fr.mgargadennec.blossom.core.role.process.IBlossomRoleProcess;
import fr.mgargadennec.blossom.core.role.service.IBlossomRoleService;
import fr.mgargadennec.blossom.core.role.service.impl.BlossomRoleServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomRoleServiceConfiguration {

  @Autowired
  private BlossomRoleProperties roleProperties;

  @Bean
  public IBlossomRoleService blossomRoleService(IBlossomRoleProcess blossomRoleProcess,
      IBlossomElasticsearchQueryService esService, IBlossomAuthenticationUtilService boAuthenticationUtilService,
      ApplicationEventPublisher eventPublisher,
      @Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_REGISTRY) PluginRegistry<IBlossomESResourceIndexBuilder, String> esResourceIndexBuilderRegistry) {

    return new BlossomRoleServiceImpl(blossomRoleProcess, esService, boAuthenticationUtilService, eventPublisher,
        esResourceIndexBuilderRegistry.getPluginFor(BlossomRoleConst.BLOSSOM_ROLE_ENTITY_NAME));
  }

}
