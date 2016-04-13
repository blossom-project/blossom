package fr.mgargadennec.blossom.core.right.configuration.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.right.support.entity.definition.impl.BlossomRightEntityDefinition;

@Configuration
public class BlossomRightEntityDefinitionConfiguration {
	
	@Bean
	public BlossomRightEntityDefinition blossomRightEntityDefinition() {
		return new BlossomRightEntityDefinition();
	}
	

}
