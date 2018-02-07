package com.blossom_project.autoconfigure.ui.web.system;

import com.blossom_project.core.common.utils.privilege.Privilege;
import com.blossom_project.core.common.utils.privilege.SimplePrivilege;
import com.blossom_project.ui.menu.MenuItem;
import com.blossom_project.ui.menu.MenuItemBuilder;
import com.blossom_project.ui.web.system.liquibase.LiquibaseController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.autoconfigure.liquibase.LiquibaseEndpointAutoConfiguration;
import org.springframework.boot.actuate.liquibase.LiquibaseEndpoint;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@AutoConfigureAfter({LiquibaseEndpointAutoConfiguration.class})
@ConditionalOnClass(LiquibaseController.class)
@ConditionalOnBean(LiquibaseEndpoint.class)
public class WebSystemLiquibaseAutoConfiguration {

  @Bean
  public MenuItem systemLiquibaseMenuItem(MenuItemBuilder builder,
    @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder
      .key("liquibase")
      .label("menu.system.liquibase")
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
    return new SimplePrivilege("system", "liquibase", "manager");
  }
}
