package fr.mgargadennec.blossom.autoconfigure.role;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.security.BlossomSecurityAutoConfiguration;
import fr.mgargadennec.blossom.core.role.configuration.root.BlossomRoleConfiguration;
import fr.mgargadennec.blossom.core.role.configuration.root.properties.BlossomRoleProperties;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter(BlossomCommonAutoConfiguration.class)
@EnableConfigurationProperties(BlossomRoleProperties.class)
@Import(BlossomRoleConfiguration.class)
public class BlossomRoleAutoConfiguration {

}
