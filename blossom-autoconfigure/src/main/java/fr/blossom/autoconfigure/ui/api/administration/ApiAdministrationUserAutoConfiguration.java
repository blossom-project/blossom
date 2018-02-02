package fr.blossom.autoconfigure.ui.api.administration;

import fr.blossom.autoconfigure.ui.common.privileges.UserPrivilegesConfiguration;
import fr.blossom.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import fr.blossom.core.common.search.SearchEngineImpl;
import fr.blossom.core.user.UserDTO;
import fr.blossom.core.user.UserService;
import fr.blossom.ui.api.administration.UsersApiController;
import org.apache.tika.Tika;
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
@ConditionalOnClass({UserService.class, UsersApiController.class})
@ConditionalOnBean(UserService.class)
@AutoConfigureAfter(WebInterfaceAutoConfiguration.class)
@Import(UserPrivilegesConfiguration.class)
public class ApiAdministrationUserAutoConfiguration {

  @Bean
  public UsersApiController usersApiController(UserService userService,
    SearchEngineImpl<UserDTO> searchEngine, Tika tika) {
    return new UsersApiController(userService, searchEngine, tika);
  }

}
