package fr.mgargadennec.blossom.autoconfigure.association.group.entity;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.group.BlossomGroupAutoConfiguration;
import fr.mgargadennec.blossom.core.association.group.entity.configuration.root.BlossomAssociationGroupEntityConfiguration;
import fr.mgargadennec.blossom.core.association.group.entity.configuration.root.properties.BlossomAssociationGroupEntityProperties;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter({BlossomCommonAutoConfiguration.class, BlossomGroupAutoConfiguration.class})
@EnableConfigurationProperties(BlossomAssociationGroupEntityProperties.class)
@Import(BlossomAssociationGroupEntityConfiguration.class)
public class BlossomAssociationGroupEntityAutoConfiguration {

}
