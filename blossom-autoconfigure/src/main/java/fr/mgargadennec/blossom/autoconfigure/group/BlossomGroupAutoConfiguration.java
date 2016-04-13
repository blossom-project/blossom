package fr.mgargadennec.blossom.autoconfigure.group;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.security.BlossomSecurityAutoConfiguration;
import fr.mgargadennec.blossom.core.group.configuration.root.BlossomGroupConfiguration;
import fr.mgargadennec.blossom.core.group.configuration.root.properties.BlossomGroupProperties;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter(BlossomCommonAutoConfiguration.class)
@EnableConfigurationProperties(BlossomGroupProperties.class)
@Import(BlossomGroupConfiguration.class)
public class BlossomGroupAutoConfiguration {

}
