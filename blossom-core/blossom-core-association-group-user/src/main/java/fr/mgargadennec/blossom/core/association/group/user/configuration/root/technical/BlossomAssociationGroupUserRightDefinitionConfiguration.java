package fr.mgargadennec.blossom.core.association.group.user.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.group.user.support.security.impl.BlossomAssociationGroupUserRightDefinition;

@Configuration
public class BlossomAssociationGroupUserRightDefinitionConfiguration {
	
	@Bean
	public BlossomAssociationGroupUserRightDefinition blossomAssociationGroupUserRightDefinition() {
		return new BlossomAssociationGroupUserRightDefinition();
	}
	

}
