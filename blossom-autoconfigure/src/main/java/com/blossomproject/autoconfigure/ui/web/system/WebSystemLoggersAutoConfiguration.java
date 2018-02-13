package com.blossomproject.autoconfigure.ui.web.system;

import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.ui.menu.MenuItem;
import com.blossomproject.ui.menu.MenuItemBuilder;
import com.blossomproject.ui.web.system.logger.LoggerManagerController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.logging.LoggersEndpointAutoConfiguration;
import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@AutoConfigureAfter(LoggersEndpointAutoConfiguration.class)
@ConditionalOnClass(LoggerManagerController.class)
@ConditionalOnBean(LoggersEndpoint.class)
public class WebSystemLoggersAutoConfiguration {

  @Bean
  public MenuItem systemLoggerManagerMenuItem(MenuItemBuilder builder,
    @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder.key("loggerManager")
      .label("menu.system.loggers")
      .link("/blossom/system/loggers")
      .order(3)
      .icon("fa fa-pencil")
      .privilege(loggersPrivilegePlugin())
      .parent(systemMenuItem)
      .build();
  }


  @Bean
  public LoggerManagerController loggerManagerController(LoggersEndpoint loggersEndpoint) {
    return new LoggerManagerController(loggersEndpoint);
  }

  @Bean
  public Privilege loggersPrivilegePlugin() {
    return new SimplePrivilege("system", "loggers", "manager");
  }

}
