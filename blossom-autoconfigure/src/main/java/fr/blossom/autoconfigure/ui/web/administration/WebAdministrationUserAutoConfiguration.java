package fr.blossom.autoconfigure.ui.web.administration;

import fr.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.administration.user.UsersController;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(UsersController.class)
@AutoConfigureAfter(CommonAutoConfiguration.class)
public class WebAdministrationUserAutoConfiguration {

  @Bean
  public MenuItem administrationUserMenuItem(MenuItemBuilder builder,
    @Qualifier("administrationMenuItem") MenuItem administrationMenuItem) {
    return builder
      .key("users")
      .label("menu.administration.users")
      .link("/blossom/administration/users")
      .icon("fa fa-user")
      .order(1)
      .privilege(usersReadPrivilege())
      .parent(administrationMenuItem)
      .build();
  }

  @Bean
  public UsersController usersController(UserService userService, Tika tika,
    SearchEngineImpl<UserDTO> searchEngine) {
    return new UsersController(userService, searchEngine, tika);
  }


  @Bean
  public Privilege usersReadPrivilege() {
    return new SimplePrivilege("administration", "users", "read");
  }

  @Bean
  public Privilege usersWritePrivilege() {
    return new SimplePrivilege("administration", "users", "write");
  }

  @Bean
  public Privilege usersCreatePrivilege() {
    return new SimplePrivilege("administration", "users", "create");
  }

  @Bean
  public Privilege usersDeletePrivilege() {
    return new SimplePrivilege("administration", "users", "delete");
  }

}
