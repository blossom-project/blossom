package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.scheduler.history.TriggerHistoryDao;
import com.blossomproject.core.scheduler.history.TriggerHistoryDaoImpl;
import com.blossomproject.core.scheduler.job.ScheduledJobService;
import com.blossomproject.core.scheduler.job.ScheduledJobServiceImpl;
import com.blossomproject.core.scheduler.listener.GlobalTriggerListener;
import com.blossomproject.core.scheduler.supervision.JobExecutionHealthIndicator;
import org.quartz.Scheduler;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
public class SchedulerAutoConfiguration {

  @Configuration
  @AutoConfigureBefore(QuartzAutoConfiguration.class)
  public static class GlobalListenerQuartzAutoConfiguration {
    @Bean
    public TriggerHistoryDao triggerHistoryDao(JdbcTemplate jdbcTemplate) {
      return new TriggerHistoryDaoImpl(jdbcTemplate, 10);
    }

    @Bean
    public GlobalTriggerListener globalTriggerListener(TriggerHistoryDao triggerHistoryDao) {
      return new GlobalTriggerListener(triggerHistoryDao);
    }

  }

  @Configuration
  @AutoConfigureAfter(GlobalListenerQuartzAutoConfiguration.class)
  public static class CustomizerQuartzAutoConfiguration implements SchedulerFactoryBeanCustomizer {
    private final GlobalTriggerListener globalTriggerListener;

    public CustomizerQuartzAutoConfiguration(GlobalTriggerListener globalTriggerListener) {
      this.globalTriggerListener = globalTriggerListener;
    }

    @Override
    public void customize(SchedulerFactoryBean schedulerFactoryBean) {
      schedulerFactoryBean.setGlobalTriggerListeners(globalTriggerListener);
    }
  }


  @Configuration
  @AutoConfigureAfter(QuartzAutoConfiguration.class)
  public static class JobServiceQuartzAutoConfiguration {
    @Bean
    public ScheduledJobService scheduledJobService(Scheduler scheduler, TriggerHistoryDao triggerHistoryDao) {
      return new ScheduledJobServiceImpl(scheduler, triggerHistoryDao);
    }

    @Bean
    public HealthIndicator quartzJobExecutionHealthIndicator(ScheduledJobService scheduledJobService) {
      return new JobExecutionHealthIndicator(scheduledJobService);
    }
  }

}
