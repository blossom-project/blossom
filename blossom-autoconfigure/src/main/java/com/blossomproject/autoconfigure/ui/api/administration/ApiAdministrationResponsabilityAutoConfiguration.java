package com.blossomproject.autoconfigure.ui.api.administration;

import com.blossomproject.autoconfigure.ui.common.privileges.ResponsabilityPrivilegesConfiguration;
import com.blossomproject.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossomproject.core.association_user_role.AssociationUserRoleService;
import com.blossomproject.core.role.RoleService;
import com.blossomproject.core.user.UserService;
import com.blossomproject.ui.api.administration.ResponsabilitiesApiController;
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
