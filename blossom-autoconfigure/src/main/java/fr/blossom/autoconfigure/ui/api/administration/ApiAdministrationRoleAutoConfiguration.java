package fr.blossom.autoconfigure.ui.api.administration;

import fr.blossom.autoconfigure.ui.common.privileges.RolePrivilegesConfiguration;
import fr.blossom.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.role.RoleDTO;
import fr.blossom.core.role.RoleService;
import fr.blossom.ui.api.administration.RolesApiController;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass({RoleService.class, RolesApiController.class})
@ConditionalOnBean(RoleService.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(RolePrivilegesConfiguration.class)
public class ApiAdministrationRoleAutoConfiguration {

  @Bean
  public RolesApiController rolesApiController(RoleService roleService,
    SearchEngineImpl<RoleDTO> searchEngine) {
    return new RolesApiController(roleService, searchEngine);
  }

}
