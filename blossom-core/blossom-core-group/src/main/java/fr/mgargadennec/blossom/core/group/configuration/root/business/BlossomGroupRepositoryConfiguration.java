package fr.mgargadennec.blossom.core.group.configuration.root.business;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "fr.mgargadennec.blossom.core.group.repository")
public class BlossomGroupRepositoryConfiguration {

}
