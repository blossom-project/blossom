package fr.mgargadennec.blossom.core.association.user.role.configuration.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.user.role.configuration.properties.BlossomAssociationUserRoleProperties;
import fr.mgargadennec.blossom.core.association.user.role.process.IBlossomAssociationUserRoleProcess;
import fr.mgargadennec.blossom.core.association.user.role.service.IBlossomAssociationUserRoleService;
import fr.mgargadennec.blossom.core.association.user.role.service.impl.BlossomAssociationUserRoleServiceImpl;
import fr.mgargadennec.blossom.core.role.service.IBlossomRoleService;
import fr.mgargadennec.blossom.core.user.service.IBlossomUserService;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomAssociationUserRoleServiceConfiguration {

	@Autowired
	private BlossomAssociationUserRoleProperties associationUserRoleProperties;

	@Bean
	public IBlossomAssociationUserRoleService blossomAssociationUserRoleService(
			IBlossomAssociationUserRoleProcess blossomAssociationUserRoleProcess,
			IBlossomUserService blossomUserService, 
			IBlossomRoleService blossomRoleService,
			IBlossomAuthenticationUtilService blossomAuthenticationUtilService, 
			ApplicationEventPublisher eventPublisher) {

		return new BlossomAssociationUserRoleServiceImpl(blossomAssociationUserRoleProcess, blossomUserService, blossomRoleService,
				blossomAuthenticationUtilService, eventPublisher);
	}

}
