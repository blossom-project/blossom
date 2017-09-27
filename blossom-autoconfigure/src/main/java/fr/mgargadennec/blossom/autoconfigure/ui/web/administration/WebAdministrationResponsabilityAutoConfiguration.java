package fr.mgargadennec.blossom.autoconfigure.ui.web.administration;

import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.common.utils.privilege.SimplePrivilege;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.association_user_role.AssociationUserRoleService;
import fr.mgargadennec.blossom.core.role.RoleService;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.web.administration.responsability.ResponsabilitiesController;

@Configuration
@ConditionalOnClass(ResponsabilitiesController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationResponsabilityAutoConfiguration {

  @Bean
  public ResponsabilitiesController responsabilityController(UserService userService,
      AssociationUserRoleService associationUserRoleService, RoleService roleService) {
    return new ResponsabilitiesController(userService, associationUserRoleService, roleService);
  }


  @Bean
  public Privilege responsabilitiesReadPrivilegePlugin() {
    return new SimplePrivilege("administration", "responsabilities", "read");
  }

  @Bean
  public Privilege responsabilitiesChangePrivilegePlugin() {
    return new SimplePrivilege("administration", "responsabilities", "change");
  }

}
