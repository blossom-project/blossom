package fr.mgargadennec.blossom.autoconfigure.association.group.user;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.group.BlossomGroupAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.user.BlossomUserAutoConfiguration;
import fr.mgargadennec.blossom.core.association.group.user.configuration.root.BlossomAssociationGroupUserConfiguration;
import fr.mgargadennec.blossom.core.association.group.user.configuration.root.properties.BlossomAssociationGroupUserProperties;

@Configuration
@AutoConfigureOrder(value = Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter({ BlossomCommonAutoConfiguration.class, BlossomUserAutoConfiguration.class,
		BlossomGroupAutoConfiguration.class })
@EnableConfigurationProperties(BlossomAssociationGroupUserProperties.class)
@Import(BlossomAssociationGroupUserConfiguration.class)
public class BlossomAssociationGroupUserAutoConfiguration {

}
