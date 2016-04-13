package fr.mgargadennec.blossom.core.association.group.entity.configuration.root.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;

import fr.mgargadennec.blossom.core.association.group.entity.configuration.root.properties.BlossomAssociationGroupEntityProperties;
import fr.mgargadennec.blossom.core.association.group.entity.process.IBlossomAssociationGroupEntityProcess;
import fr.mgargadennec.blossom.core.association.group.entity.service.IBlossomAssociationGroupEntityService;
import fr.mgargadennec.blossom.core.association.group.entity.service.impl.BlossomAssociationGroupEntityServiceImpl;
import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.service.IBlossomServicePlugin;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomAssociationGroupEntityServiceConfiguration {

	@Autowired
	private BlossomAssociationGroupEntityProperties roleProperties;

	@Bean
	public IBlossomAssociationGroupEntityService blossomAssociationGroupEntityService(
			IBlossomAssociationGroupEntityProcess boAssociationGroupEntityProcess,
			IBlossomAuthenticationUtilService boAuthenticationUtilService, ApplicationEventPublisher eventPublisher,
			@Qualifier(BlossomConst.PLUGIN_QUALIFIER_BLOSSOM_SERVICE_BUILDER_REGISTRY) PluginRegistry<IBlossomServicePlugin, String> servicePluginRegistry) {

		return new BlossomAssociationGroupEntityServiceImpl(boAssociationGroupEntityProcess, servicePluginRegistry,
				boAuthenticationUtilService, eventPublisher);
	}

}
