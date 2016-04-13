package fr.mgargadennec.blossom.core.user.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.user.support.entity.definition.impl.BlossomUserEntityDefinition;
import fr.mgargadennec.blossom.core.user.support.history.builder.impl.BlossomUserHistoryBuilderImpl;

@Configuration
public class BlossomUserEntityDefinitionConfiguration {
	
	@Bean
	public BlossomUserEntityDefinition blossomUserEntityDefinition() {
		return new BlossomUserEntityDefinition();
	}
	

}
