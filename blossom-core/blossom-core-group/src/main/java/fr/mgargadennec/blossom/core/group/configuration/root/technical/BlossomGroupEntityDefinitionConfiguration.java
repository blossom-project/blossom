package fr.mgargadennec.blossom.core.group.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.group.support.entity.definition.impl.BlossomGroupEntityDefinition;

@Configuration
public class BlossomGroupEntityDefinitionConfiguration {
	
	@Bean
	public BlossomGroupEntityDefinition blossomGroupEntityDefinition() {
		return new BlossomGroupEntityDefinition();
	}
	

}
