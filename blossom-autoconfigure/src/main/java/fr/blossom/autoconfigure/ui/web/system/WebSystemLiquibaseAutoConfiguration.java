package fr.blossom.autoconfigure.ui.web.system;

import fr.blossom.autoconfigure.core.LiquibaseAutoConfiguration;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.system.dashboard.DashboardController;
import fr.blossom.ui.web.system.liquibase.LiquibaseController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.endpoint.LiquibaseEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@AutoConfigureAfter({LiquibaseAutoConfiguration.class})
@ConditionalOnClass(LiquibaseController.class)
@ConditionalOnBean(LiquibaseEndpoint.class)
public class WebSystemLiquibaseAutoConfiguration {

  @Bean
  public MenuItem systemLiquibaseMenuItem(MenuItemBuilder builder, @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder
      .key("liquibase")
      .label("menu.system.liquibase", true)
      .link("/blossom/system/liquibase")
      .order(4)
      .icon("fa fa-flask")
      .parent(systemMenuItem)
      .privilege(liquibasePrivilegePlugin())
      .build();
  }

  @Bean
  public LiquibaseController liquibaseController(LiquibaseEndpoint liquibaseEndpoint) {
    return new LiquibaseController(liquibaseEndpoint);
  }

  @Bean
  public Privilege liquibasePrivilegePlugin() {
    return new SimplePrivilege("system","liquibase", "manager");
  }
}
