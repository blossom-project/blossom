package fr.mgargadennec.blossom.core.group.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.group.support.history.builder.impl.BlossomGroupHistoryBuilderImpl;

@Configuration
public class BlossomGroupHistoryBuilderConfiguration {

	@Bean
	public BlossomGroupHistoryBuilderImpl blossomGroupHistoryBuilderImpl(
			IBlossomEntityDiffBuilder blossomEntityDiffBuilder) {
		return new BlossomGroupHistoryBuilderImpl(blossomEntityDiffBuilder);
	}

}
