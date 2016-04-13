package fr.mgargadennec.blossom.autoconfigure.user;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonWebAutoConfiguration;
import fr.mgargadennec.blossom.core.user.configuration.web.BlossomUserWebConfiguration;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter(BlossomCommonWebAutoConfiguration.class)
@Import(BlossomUserWebConfiguration.class)
public class BlossomUserWebAutoConfiguration {
}
