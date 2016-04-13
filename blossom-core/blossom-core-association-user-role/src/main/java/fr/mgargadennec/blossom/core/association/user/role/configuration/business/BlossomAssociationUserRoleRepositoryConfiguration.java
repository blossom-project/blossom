package fr.mgargadennec.blossom.core.association.user.role.configuration.business;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "fr.mgargadennec.blossom.core.association.user.role.repository")
public class BlossomAssociationUserRoleRepositoryConfiguration {

}
