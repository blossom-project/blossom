package fr.blossom.autoconfigure.ui.web.system;

import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.core.scheduler.job.ScheduledJobService;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.system.scheduler.SchedulerController;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnBean(Scheduler.class)
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
