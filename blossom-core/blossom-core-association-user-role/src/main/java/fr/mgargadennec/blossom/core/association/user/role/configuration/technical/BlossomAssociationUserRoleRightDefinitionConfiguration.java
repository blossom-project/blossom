package fr.mgargadennec.blossom.core.association.user.role.configuration.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.user.role.support.security.impl.BlossomAssociationUserRoleRightDefinition;

@Configuration
public class BlossomAssociationUserRoleRightDefinitionConfiguration {
	
	@Bean
	public BlossomAssociationUserRoleRightDefinition blossomAssociationUserRoleRightDefinition() {
		return new BlossomAssociationUserRoleRightDefinition();
	}
	

}
