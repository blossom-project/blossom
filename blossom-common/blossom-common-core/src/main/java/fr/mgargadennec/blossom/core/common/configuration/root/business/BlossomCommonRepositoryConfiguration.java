package fr.mgargadennec.blossom.core.common.configuration.root.business;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "fr.mgargadennec.blossom.core.common.repository")
public class BlossomCommonRepositoryConfiguration {

}
