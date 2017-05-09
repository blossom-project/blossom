package fr.mgargadennec.blossom.autoconfigure.ui.web.system;

import fr.mgargadennec.blossom.core.scheduler.job.ScheduledJobServiceImpl;
import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.system.scheduler.SchedulerController;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
public class WebSystemSchedulerAutoConfiguration {

  @Bean
  public MenuItem systemSchedulerMenuItem(MenuItemBuilder builder, @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder.key("scheduler").label("menu.system.scheduler", true).link("/blossom/system/scheduler").order(2).icon("fa fa-calendar").parent(systemMenuItem).build();
  }

  @Bean
  public SchedulerController schedulerController(ScheduledJobServiceImpl scheduledJobService){
    return new SchedulerController(scheduledJobService);
  }
}
