package fr.mgargadennec.blossom.core.association.user.role.configuration.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.user.role.support.entity.definition.impl.BlossomAssociationUserRoleEntityDefinition;

@Configuration
public class BlossomAssociationUserRoleEntityDefinitionConfiguration {
	
	@Bean
	public BlossomAssociationUserRoleEntityDefinition blossomAssociationUserRoleEntityDefinition() {
		return new BlossomAssociationUserRoleEntityDefinition();
	}
	

}
