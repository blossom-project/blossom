package fr.blossom.autoconfigure.ui.web.administration;

import fr.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.blossom.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import fr.blossom.core.association_user_role.AssociationUserRoleService;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.core.role.RoleService;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.web.administration.responsability.ResponsabilitiesController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass(ResponsabilitiesController.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
public class WebAdministrationResponsabilityAutoConfiguration {

    @Bean
    public ResponsabilitiesController responsabilityController(UserService userService, AssociationUserRoleService associationUserRoleService, RoleService roleService) {
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
