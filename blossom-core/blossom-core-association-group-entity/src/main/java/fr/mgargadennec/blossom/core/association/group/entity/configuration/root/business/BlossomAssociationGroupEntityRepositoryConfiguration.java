package fr.mgargadennec.blossom.core.association.group.entity.configuration.root.business;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "fr.mgargadennec.blossom.core.association.group.entity.repository")
public class BlossomAssociationGroupEntityRepositoryConfiguration {

}
