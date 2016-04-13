package fr.mgargadennec.blossom.core.right.configuration.business;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "fr.mgargadennec.blossom.core.right.model")
public class BlossomRightEntityConfiguration {

}
