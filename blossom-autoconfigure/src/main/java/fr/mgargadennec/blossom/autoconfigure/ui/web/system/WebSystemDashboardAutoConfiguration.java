package fr.mgargadennec.blossom.autoconfigure.ui.web.system;

import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.system.dashboard.DashboardController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.endpoint.Endpoint;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnBean(Endpoint.class)
public class WebSystemDashboardAutoConfiguration {

  @Bean
  public MenuItem systemDashboardMenuItem(MenuItemBuilder builder, @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder.key("dashboard").label("menu.system.dashboard", true).link("/blossom/system/dashboard").order(1).icon("fa fa-bar-chart").parent(systemMenuItem).build();
  }

  @Bean
  public DashboardController dashboardController(HealthEndpoint healthEndpoint, MetricsEndpoint metricsEndpoint) {
    return new DashboardController(healthEndpoint, metricsEndpoint);
  }
}
