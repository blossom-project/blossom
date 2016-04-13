package fr.mgargadennec.blossom.autoconfigure.association.group.user;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonWebAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.group.BlossomGroupWebAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.user.BlossomUserWebAutoConfiguration;
import fr.mgargadennec.blossom.core.association.group.user.configuration.web.BlossomAssociationGroupUserWebConfiguration;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter({ BlossomCommonWebAutoConfiguration.class, BlossomUserWebAutoConfiguration.class,
	BlossomGroupWebAutoConfiguration.class })
@Import(BlossomAssociationGroupUserWebConfiguration.class)
public class BlossomAssociationGroupUserWebAutoConfiguration {
}
