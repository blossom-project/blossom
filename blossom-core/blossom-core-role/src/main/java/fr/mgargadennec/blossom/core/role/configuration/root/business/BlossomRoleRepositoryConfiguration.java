package fr.mgargadennec.blossom.core.role.configuration.root.business;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "fr.mgargadennec.blossom.core.role.repository")
public class BlossomRoleRepositoryConfiguration {

}
