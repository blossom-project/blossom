package fr.mgargadennec.blossom.core.user.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.user.support.security.impl.BlossomUserRightDefinition;

@Configuration
public class BlossomUserRightDefinitionConfiguration {
	
	@Bean
	public BlossomUserRightDefinition blossomUserRightDefinition() {
		return new BlossomUserRightDefinition();
	}
	

}
