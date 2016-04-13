package fr.mgargadennec.blossom.autoconfigure.right;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.core.right.configuration.BlossomRightConfiguration;
import fr.mgargadennec.blossom.core.right.configuration.properties.BlossomRightProperties;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter(BlossomCommonAutoConfiguration.class)
@EnableConfigurationProperties(BlossomRightProperties.class)
@Import(BlossomRightConfiguration.class)
public class BlossomRightAutoConfiguration {

}
