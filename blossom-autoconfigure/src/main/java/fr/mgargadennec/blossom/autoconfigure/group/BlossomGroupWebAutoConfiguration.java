package fr.mgargadennec.blossom.autoconfigure.group;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonWebAutoConfiguration;
import fr.mgargadennec.blossom.core.group.configuration.web.BlossomGroupWebConfiguration;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter(BlossomCommonWebAutoConfiguration.class)
@Import(BlossomGroupWebConfiguration.class)
public class BlossomGroupWebAutoConfiguration {
}
