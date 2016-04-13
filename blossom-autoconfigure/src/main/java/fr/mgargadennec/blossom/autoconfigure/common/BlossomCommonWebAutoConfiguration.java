package fr.mgargadennec.blossom.autoconfigure.common;

import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.core.common.configuration.web.BlossomCommonWebConfiguration;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@Import(BlossomCommonWebConfiguration.class)
public class BlossomCommonWebAutoConfiguration {
}
