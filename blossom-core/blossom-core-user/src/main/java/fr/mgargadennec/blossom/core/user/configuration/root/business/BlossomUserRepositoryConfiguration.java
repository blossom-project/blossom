package fr.mgargadennec.blossom.core.user.configuration.root.business;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "fr.mgargadennec.blossom.core.user.repository")
public class BlossomUserRepositoryConfiguration {

}
