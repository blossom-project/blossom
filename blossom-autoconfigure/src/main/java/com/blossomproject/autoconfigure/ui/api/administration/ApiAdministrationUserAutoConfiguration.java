package com.blossomproject.autoconfigure.ui.api.administration;

import com.blossomproject.autoconfigure.ui.common.privileges.UserPrivilegesConfiguration;
import com.blossomproject.autoconfigure.ui.web.WebInterfaceAutoConfiguration;
import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.core.user.UserDTO;
import com.blossomproject.core.user.UserService;
import com.blossomproject.ui.api.administration.UsersApiController;
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
