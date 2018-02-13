package com.blossomproject.autoconfigure.ui.web.system;

import com.blossomproject.autoconfigure.core.SchedulerAutoConfiguration;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.core.scheduler.job.ScheduledJobService;
import com.blossomproject.ui.menu.MenuItem;
import com.blossomproject.ui.menu.MenuItemBuilder;
import com.blossomproject.ui.web.system.scheduler.SchedulerController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@AutoConfigureAfter(SchedulerAutoConfiguration.class)
@ConditionalOnClass(SchedulerController.class)
@ConditionalOnBean(ScheduledJobService.class)
public class WebSystemSchedulerAutoConfiguration {

  @Bean
  public MenuItem systemSchedulerMenuItem(MenuItemBuilder builder,
    @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder
      .key("schedulerManager")
      .label("menu.system.scheduler")
      .link("/blossom/system/scheduler")
      .order(1)
      .icon("fa fa-calendar")
      .privilege(schedulerPrivilegePlugin())
      .parent(systemMenuItem).build();
  }

  @Bean
  public SchedulerController schedulerController(ScheduledJobService scheduledJobService) {
    return new SchedulerController(scheduledJobService);
  }

  @Bean
  public Privilege schedulerPrivilegePlugin() {
    return new SimplePrivilege("system", "scheduler", "manager");
  }
}
