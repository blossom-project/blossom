package fr.mgargadennec.blossom.autoconfigure.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.plugin.core.PluginRegistry;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.security.none.BlossomAuthenticationUtilServiceNoneImpl;
import fr.mgargadennec.blossom.core.association.group.entity.repository.BlossomAssociationGroupEntityRepository;
import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.support.entity.definition.IBlossomEntityDefinition;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomSecurityScopeDelegate;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomSecurityUtilService;
import fr.mgargadennec.blossom.security.support.security.impl.BlossomAuthenticationUtilServiceGroupBasedImpl;

@Configuration
@AutoConfigureOrder(value = Ordered.LOWEST_PRECEDENCE)
@AutoConfigureBefore(BlossomCommonAutoConfiguration.class)
public class BlossomSecurityAutoConfiguration {

  @Configuration
  @ConditionalOnMissingBean(IBlossomAuthenticationUtilService.class)
  @AutoConfigureAfter(BlossomSecurityGroupBasedAutoConfiguration.class)
  protected static class BlossomSecurityNoneAutoConfiguration {

    @Autowired
    @Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_ENTITY_DEFINITION_REGISTRY)
    protected PluginRegistry<IBlossomEntityDefinition, String> entityDefinitionRegistry;

    @Bean
    public IBlossomAuthenticationUtilService authenticationUtil() {
      return new BlossomAuthenticationUtilServiceNoneImpl(entityDefinitionRegistry);
    }
  }

  @Configuration
  @ConditionalOnClass(BlossomAuthenticationUtilServiceGroupBasedImpl.class)
  @ConditionalOnBean(BlossomAssociationGroupEntityRepository.class)
  protected static class BlossomSecurityGroupBasedAutoConfiguration {

    @Autowired
    @Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_ENTITY_DEFINITION_REGISTRY)
    protected PluginRegistry<IBlossomEntityDefinition, String> entityDefinitionRegistry;

    @Autowired
    @Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_SECURITY_SCOPE_DELEGATE_REGISTRY)
    private PluginRegistry<IBlossomSecurityScopeDelegate, String> scopeDelegateRegistry;

    @Bean
    public IBlossomAuthenticationUtilService authenticationUtil(
        BlossomAssociationGroupEntityRepository blossomAssociationGroupRepository,
        IBlossomSecurityUtilService blossomSecurityUtilService) {
      return new BlossomAuthenticationUtilServiceGroupBasedImpl(blossomAssociationGroupRepository,
          blossomSecurityUtilService, entityDefinitionRegistry, scopeDelegateRegistry);
    }
    
    

  }
}
