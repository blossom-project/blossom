package fr.mgargadennec.blossom.core.role.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.role.support.security.impl.BlossomRoleRightDefinition;

@Configuration
public class BlossomRoleRightDefinitionConfiguration {
	
	@Bean
	public BlossomRoleRightDefinition blossomRoleRightDefinition() {
		return new BlossomRoleRightDefinition();
	}
	

}
