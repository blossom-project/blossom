package fr.mgargadennec.blossom.core.association.group.user.configuration.root.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.group.user.configuration.root.properties.BlossomAssociationGroupUserProperties;
import fr.mgargadennec.blossom.core.association.group.user.process.IBlossomAssociationGroupUserProcess;
import fr.mgargadennec.blossom.core.association.group.user.service.IBlossomAssociationGroupUserService;
import fr.mgargadennec.blossom.core.association.group.user.service.impl.BlossomAssociationGroupUserServiceImpl;
import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomAssociationGroupUserServiceConfiguration {

	@Autowired
	private BlossomAssociationGroupUserProperties associationUserRoleProperties;

	@Bean
	public IBlossomAssociationGroupUserService blossomAssociationGroupUserService(
			IBlossomAssociationGroupUserProcess blossomAssociationGroupUserProcess,
			IBlossomUserService blossomUserService, 
			IBlossomGroupService blossomGroupService,
			IBlossomAuthenticationUtilService blossomAuthenticationUtilService, 
			ApplicationEventPublisher eventPublisher) {

		return new BlossomAssociationGroupUserServiceImpl(blossomAssociationGroupUserProcess, blossomGroupService, blossomUserService, 
				blossomAuthenticationUtilService, eventPublisher);
	}

}
