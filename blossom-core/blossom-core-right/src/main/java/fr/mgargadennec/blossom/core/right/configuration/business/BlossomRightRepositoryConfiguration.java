package fr.mgargadennec.blossom.core.right.configuration.business;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "fr.mgargadennec.blossom.core.right.repository")
public class BlossomRightRepositoryConfiguration {

}
