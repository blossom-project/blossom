package fr.mgargadennec.blossom.core.user.configuration.root.business;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.common.support.mail.IBlossomMailService;
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.IBlossomElasticsearchQueryService;
import fr.mgargadennec.blossom.core.user.constants.BlossomUserConst;
import fr.mgargadennec.blossom.core.user.process.IBlossomUserProcess;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.core.user.service.impl.BlossomUserServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomUserServiceConfiguration {

  @Bean
  public IBlossomUserService blossomUserService(
      IBlossomUserProcess blossomUserProcess,
      IBlossomElasticsearchQueryService esService, 
      IBlossomMailService blossomMailService,
      IBlossomAuthenticationUtilService boAuthenticationUtilService,
      ApplicationEventPublisher eventPublisher,
      @Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_RESOURCE_INDEX_BUILDER_REGISTRY) PluginRegistry<IBlossomESResourceIndexBuilder, String> esResourceIndexBuilderRegistry) {

    return new BlossomUserServiceImpl(blossomUserProcess, esService, boAuthenticationUtilService, eventPublisher,
        esResourceIndexBuilderRegistry.getPluginFor(BlossomUserConst.BLOSSOM_USER_ENTITY_NAME));
  }

}
