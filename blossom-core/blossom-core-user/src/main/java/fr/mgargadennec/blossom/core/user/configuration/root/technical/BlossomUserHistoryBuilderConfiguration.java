package fr.mgargadennec.blossom.core.user.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomEntityDiffBuilder;
import fr.mgargadennec.blossom.core.user.support.history.builder.impl.BlossomUserHistoryBuilderImpl;

@Configuration
public class BlossomUserHistoryBuilderConfiguration {

	@Bean
	public BlossomUserHistoryBuilderImpl blossomUserHistoryBuilderImpl(
			IBlossomEntityDiffBuilder blossomEntityDiffBuilder) {
		return new BlossomUserHistoryBuilderImpl(blossomEntityDiffBuilder);
	}

}
