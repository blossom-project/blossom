package com.blossom_project.autoconfigure.ui.web.administration;

import com.blossom_project.autoconfigure.ui.common.privileges.RolePrivilegesConfiguration;
import com.blossom_project.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossom_project.core.common.search.SearchEngineImpl;
import com.blossom_project.core.role.RoleDTO;
import com.blossom_project.core.role.RoleService;
import com.blossom_project.ui.menu.MenuItem;
import com.blossom_project.ui.menu.MenuItemBuilder;
import com.blossom_project.ui.web.administration.role.RolesController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass({RoleService.class, RolesController.class})
@ConditionalOnBean(RoleService.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(RolePrivilegesConfiguration.class)
public class WebAdministrationRoleAutoConfiguration {

  private final RolePrivilegesConfiguration rolePrivilegesConfiguration;

  public WebAdministrationRoleAutoConfiguration(
    RolePrivilegesConfiguration rolePrivilegesConfiguration) {
    this.rolePrivilegesConfiguration = rolePrivilegesConfiguration;
  }

  @Bean
    public MenuItem administrationRoleMenuItem(MenuItemBuilder builder,
                                               @Qualifier("administrationMenuItem") MenuItem administrationMenuItem) {
        return builder
                .key("roles")
                .label("menu.administration.roles")
                .link("/blossom/administration/roles")
                .icon("fa fa-key")
                .privilege(rolePrivilegesConfiguration.rolesReadPrivilegePlugin())
                .order(3)
                .parent(administrationMenuItem).build();
    }

    @Bean
    public RolesController rolesController(RoleService roleService, SearchEngineImpl<RoleDTO> searchEngine, MessageSource messageSource) {
        return new RolesController(roleService, searchEngine, messageSource);
    }

}
