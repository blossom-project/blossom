package fr.mgargadennec.blossom.core.group.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.group.support.security.impl.BlossomGroupRightDefinition;

@Configuration
public class BlossomGroupRightDefinitionConfiguration {
	
	@Bean
	public BlossomGroupRightDefinition blossomGroupRightDefinition() {
		return new BlossomGroupRightDefinition();
	}
	

}
