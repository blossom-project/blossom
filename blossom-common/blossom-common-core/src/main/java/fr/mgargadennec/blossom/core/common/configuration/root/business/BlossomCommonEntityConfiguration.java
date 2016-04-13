package fr.mgargadennec.blossom.core.common.configuration.root.business;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EntityScan(basePackages = "fr.mgargadennec.blossom.core.common.model")
public class BlossomCommonEntityConfiguration {

}
