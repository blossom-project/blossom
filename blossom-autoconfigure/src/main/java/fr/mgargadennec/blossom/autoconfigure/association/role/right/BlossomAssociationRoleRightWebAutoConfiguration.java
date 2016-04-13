package fr.mgargadennec.blossom.autoconfigure.association.role.right;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.Ordered;

import fr.mgargadennec.blossom.autoconfigure.common.BlossomCommonWebAutoConfiguration;
import fr.mgargadennec.blossom.autoconfigure.role.BlossomRoleWebAutoConfiguration;
import fr.mgargadennec.blossom.core.association.user.role.configuration.web.BlossomAssociationRoleRightWebConfiguration;

@Configuration
@AutoConfigureOrder(value=Ordered.LOWEST_PRECEDENCE)
@AutoConfigureAfter({BlossomCommonWebAutoConfiguration.class, BlossomRoleWebAutoConfiguration.class})
@Import(BlossomAssociationRoleRightWebConfiguration.class)
public class BlossomAssociationRoleRightWebAutoConfiguration {
}
