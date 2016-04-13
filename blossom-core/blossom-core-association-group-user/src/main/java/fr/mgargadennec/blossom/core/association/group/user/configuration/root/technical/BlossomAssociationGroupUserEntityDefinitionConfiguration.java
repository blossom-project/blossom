package fr.mgargadennec.blossom.core.association.group.user.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.association.group.user.support.entity.definition.impl.BlossomAssociationGroupUserEntityDefinition;

@Configuration
public class BlossomAssociationGroupUserEntityDefinitionConfiguration {
	
	@Bean
	public BlossomAssociationGroupUserEntityDefinition blossomAssociationGroupUserEntityDefinition() {
		return new BlossomAssociationGroupUserEntityDefinition();
	}
	

}
