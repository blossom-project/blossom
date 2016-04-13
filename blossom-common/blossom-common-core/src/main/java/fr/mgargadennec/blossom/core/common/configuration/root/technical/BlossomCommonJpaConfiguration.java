package fr.mgargadennec.blossom.core.common.configuration.root.technical;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "blossomAuditorAware")
public class BlossomCommonJpaConfiguration {
	
	@Bean
	public AuditorAware<String> blossomAuditorAware() {
		return new AuditorAware<String>() {
			@Override
			public String getCurrentAuditor() {
			  return "someone";
			}
		};
	}
	
}
