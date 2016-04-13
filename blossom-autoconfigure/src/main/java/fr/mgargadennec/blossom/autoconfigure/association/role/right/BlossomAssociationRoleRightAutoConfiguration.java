package fr.mgargadennec.blossom.autoconfigure.association.role.right;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.role.BlossomRoleAutoConfiguration;
import fr.mgargadennec.blossom.core.association.user.role.configuration.BlossomAssociationUserRoleConfiguration;
import fr.mgargadennec.blossom.core.association.user.role.configuration.root.properties.BlossomAssociationRoleRightProperties;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter({BlossomCommonAutoConfiguration.class, BlossomRoleAutoConfiguration.class})
@EnableConfigurationProperties(BlossomAssociationRoleRightProperties.class)
@Import(BlossomAssociationUserRoleConfiguration.class)
public class BlossomAssociationRoleRightAutoConfiguration {

}
