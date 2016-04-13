package fr.mgargadennec.blossom.core.common.configuration.root.technical;

import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.common.support.BlossomAutowireHelper;

@Configuration
public class BlossomCommonMiscellanousConfiguration {

	@Bean
	public BlossomAutowireHelper autowireHelper(ApplicationContext ctx) {
		BlossomAutowireHelper instance = BlossomAutowireHelper.getInstance();
		instance.setApplicationContext(ctx);
		return instance;
	}
}
