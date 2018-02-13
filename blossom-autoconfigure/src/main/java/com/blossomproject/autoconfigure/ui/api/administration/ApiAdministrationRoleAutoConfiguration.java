package com.blossomproject.autoconfigure.ui.api.administration;

import com.blossomproject.autoconfigure.ui.common.privileges.RolePrivilegesConfiguration;
import com.blossomproject.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.role.RoleDTO;
import com.blossomproject.core.role.RoleService;
import com.blossomproject.ui.api.administration.RolesApiController;
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
