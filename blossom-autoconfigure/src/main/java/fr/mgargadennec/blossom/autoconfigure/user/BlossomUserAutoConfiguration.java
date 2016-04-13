package fr.mgargadennec.blossom.autoconfigure.user;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.security.BlossomSecurityAutoConfiguration;
import fr.mgargadennec.blossom.core.user.configuration.root.BlossomUserRootConfiguration;
import fr.mgargadennec.blossom.core.user.configuration.root.properties.BlossomUserProperties;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter(BlossomCommonAutoConfiguration.class)
@EnableConfigurationProperties(BlossomUserProperties.class)
@Import(BlossomUserRootConfiguration.class)
public class BlossomUserAutoConfiguration {

}
