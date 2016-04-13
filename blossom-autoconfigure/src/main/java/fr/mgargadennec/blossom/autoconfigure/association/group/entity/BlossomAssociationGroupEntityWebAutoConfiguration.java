package fr.mgargadennec.blossom.autoconfigure.association.group.entity;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonWebAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.group.BlossomGroupWebAutoConfiguration;
import fr.mgargadennec.blossom.core.association.group.entity.configuration.web.BlossomAssociationGroupEntityWebConfiguration;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter({BlossomCommonWebAutoConfiguration.class, BlossomGroupWebAutoConfiguration.class,})
@Import(BlossomAssociationGroupEntityWebConfiguration.class)
public class BlossomAssociationGroupEntityWebAutoConfiguration {
}
