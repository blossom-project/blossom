package fr.blossom.autoconfigure.ui.api.administration;

import fr.blossom.autoconfigure.ui.common.privileges.ResponsabilityPrivilegesConfiguration;
import fr.blossom.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.role.RoleService;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.api.administration.ResponsabilitiesApiController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@ConditionalOnClass({ResponsabilitiesApiController.class})
@ConditionalOnBean({AssociationUserRoleService.class, UserService.class, RoleService.class})
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(ResponsabilityPrivilegesConfiguration.class)
public class ApiAdministrationResponsabilityAutoConfiguration {

  @Bean
  public ResponsabilitiesApiController responsabilitiesApiController(
    AssociationUserRoleService associationUserRoleService, UserService userService,
    RoleService roleService) {
    return new ResponsabilitiesApiController(associationUserRoleService, userService, roleService);
  }

}
