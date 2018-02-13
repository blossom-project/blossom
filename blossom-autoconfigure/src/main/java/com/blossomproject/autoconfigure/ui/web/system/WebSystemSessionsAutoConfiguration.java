package com.blossomproject.autoconfigure.ui.web.system;

import com.blossomproject.autoconfigure.core.CommonAutoConfiguration;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.ui.menu.MenuItem;
import com.blossomproject.ui.menu.MenuItemBuilder;
import com.blossomproject.ui.security.LoginAttemptsService;
import com.blossomproject.ui.web.system.sessions.SessionController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.session.SessionRegistry;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@AutoConfigureAfter(CommonAutoConfiguration.class)
@ConditionalOnClass(SessionController.class)
@ConditionalOnBean(SessionRegistry.class)
public class WebSystemSessionsAutoConfiguration {

  @Bean
  public MenuItem systemSessionMenuItem(MenuItemBuilder builder,
    @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder
      .key("sessions")
      .label("menu.system.sessions")
      .link("/blossom/system/sessions")
      .order(5)
      .icon("fa fa-plug")
      .parent(systemMenuItem)
      .privilege(sessionsPrivilegePlugin())
      .build();
  }

  @Bean
  public SessionController sessionController(SessionRegistry sessionRegistry,
    LoginAttemptsService loginAttemptsService) {
    return new SessionController(sessionRegistry, loginAttemptsService);
  }

  @Bean
  public Privilege sessionsPrivilegePlugin() {
    return new SimplePrivilege("system", "sessions", "manager");
  }
}
