package fr.mgargadennec.blossom.core.association.user.role.configuration.business;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "fr.mgargadennec.blossom.core.association.user.role.model")
public class BlossomAssociationUserRoleEntityConfiguration {

}
