package fr.mgargadennec.blossom.core.role.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.role.support.entity.definition.impl.BlossomRoleEntityDefinition;

@Configuration
public class BlossomRoleEntityDefinitionConfiguration {
	
	@Bean
	public BlossomRoleEntityDefinition blossomRoleEntityDefinition() {
		return new BlossomRoleEntityDefinition();
	}
	

}
