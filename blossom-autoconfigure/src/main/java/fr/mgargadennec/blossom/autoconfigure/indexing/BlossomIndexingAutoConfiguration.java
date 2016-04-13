package fr.mgargadennec.blossom.autoconfigure.indexing;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.security.BlossomSecurityAutoConfiguration;
import fr.mgargadennec.blossom.core.indexing.configuration.BlossomIndexingConfiguration;
import fr.mgargadennec.blossom.core.indexing.configuration.properties.BlossomIndexingProperties;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter(BlossomCommonAutoConfiguration.class)
@EnableConfigurationProperties(BlossomIndexingProperties.class)
@Import(BlossomIndexingConfiguration.class)
public class BlossomIndexingAutoConfiguration {

}
