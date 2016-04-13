package fr.mgargadennec.blossom.core.role.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.role.support.history.builder.impl.BlossomRoleHistoryBuilderImpl;

@Configuration
public class BlossomRoleHistoryBuilderConfiguration {

	@Bean
	public BlossomRoleHistoryBuilderImpl blossomRoleHistoryBuilderImpl(
			IBlossomEntityDiffBuilder blossomEntityDiffBuilder) {
		return new BlossomRoleHistoryBuilderImpl(blossomEntityDiffBuilder);
	}

}
