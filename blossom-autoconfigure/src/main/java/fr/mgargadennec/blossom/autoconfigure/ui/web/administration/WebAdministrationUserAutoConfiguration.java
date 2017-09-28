package fr.mgargadennec.blossom.autoconfigure.ui.web.administration;

import fr.mgargadennec.blossom.autoconfigure.core.CommonAutoConfiguration;
import fr.mgargadennec.blossom.core.common.search.SearchEngineImpl;
import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.mgargadennec.blossom.core.user.UserDTO;
import fr.mgargadennec.blossom.core.user.UserService;
import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.administration.user.UsersController;
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
      .label("menu.administration.users", true)
      .link("/blossom/administration/users")
      .icon("fa fa-user")
      .order(1)
      .privilege(usersReadPrivilege())
      .parent(administrationMenuItem)
      .build();
  }

  @Bean
  public UsersController usersController(UserService userService,
    SearchEngineImpl<UserDTO> searchEngine) {
    return new UsersController(userService, searchEngine);
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
