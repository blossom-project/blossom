package fr.mgargadennec.blossom.autoconfigure.common;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.core.common.configuration.properties.BlossomCommonProperties;
import fr.mgargadennec.blossom.core.common.configuration.root.BlossomCommonConfiguration;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@EnableConfigurationProperties(BlossomCommonProperties.class)
@Import(BlossomCommonConfiguration.class)
public class BlossomCommonAutoConfiguration {

}
