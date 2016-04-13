package fr.mgargadennec.blossom.core.role.configuration.root.business;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "fr.mgargadennec.blossom.core.role.model")
public class BlossomRoleEntityConfiguration {

}
