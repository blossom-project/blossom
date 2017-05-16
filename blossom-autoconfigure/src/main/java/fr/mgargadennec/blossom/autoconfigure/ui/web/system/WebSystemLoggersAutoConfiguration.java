package fr.mgargadennec.blossom.autoconfigure.ui.web.system;

import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.system.logger.LoggerManagerController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.LoggersEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(LoggerManagerController.class)
public class WebSystemLoggersAutoConfiguration {

  @Bean
  public MenuItem systemLoggerManagerMenuItem(MenuItemBuilder builder, @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder.key("loggerManager").label("menu.system.loggers", true).link("/blossom/system/loggers").order(4).icon("fa fa-pencil").parent(systemMenuItem).build();
  }


  @Bean
  public LoggerManagerController loggerManagerController(LoggersEndpoint loggersEndpoint) {
    return new LoggerManagerController(loggersEndpoint);
  }

}
