package fr.mgargadennec.blossom.core.association.group.entity.configuration.root.business;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "fr.mgargadennec.blossom.core.association.group.entity.model")
public class BlossomAssociationGroupEntityEntityConfiguration {

}
