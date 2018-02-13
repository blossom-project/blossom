package com.blossomproject.autoconfigure.ui.web.administration;

import com.blossomproject.autoconfigure.ui.common.privileges.UserPrivilegesConfiguration;
import com.blossomproject.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.ui.menu.MenuItem;
import com.blossomproject.ui.menu.MenuItemBuilder;
import com.blossomproject.ui.web.administration.user.UsersController;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Qualifier;
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
@ConditionalOnClass({UserService.class, UsersController.class})
@ConditionalOnBean(UserService.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(UserPrivilegesConfiguration.class)
public class WebAdministrationUserAutoConfiguration {
  private final UserPrivilegesConfiguration userPrivilegesConfiguration;

  public WebAdministrationUserAutoConfiguration(
    UserPrivilegesConfiguration userPrivilegesConfiguration) {
    this.userPrivilegesConfiguration = userPrivilegesConfiguration;
  }

  @Bean
  public MenuItem administrationUserMenuItem(MenuItemBuilder builder,
    @Qualifier("administrationMenuItem") MenuItem administrationMenuItem) {
    return builder
      .key("users")
      .label("menu.administration.users")
      .link("/blossom/administration/users")
      .icon("fa fa-user")
      .order(1)
      .privilege(userPrivilegesConfiguration.usersReadPrivilege())
      .parent(administrationMenuItem)
      .build();
  }

  @Bean
  public UsersController usersController(UserService userService, Tika tika,
    SearchEngineImpl<UserDTO> searchEngine) {
    return new UsersController(userService, searchEngine, tika);
  }
}
