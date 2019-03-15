package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.scheduler.history.*;
import com.blossomproject.core.scheduler.job.ScheduledJobService;
import com.blossomproject.core.scheduler.job.ScheduledJobServiceImpl;
import com.blossomproject.core.scheduler.listener.GlobalTriggerListener;
import com.blossomproject.core.scheduler.supervision.JobExecutionHealthIndicator;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.quartz.QuartzAutoConfiguration;
import org.springframework.boot.autoconfigure.quartz.SchedulerFactoryBeanCustomizer;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.time.Duration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
public class SchedulerAutoConfiguration {

  @Configuration
  @ConfigurationProperties("blossom.scheduler")
  @PropertySource("classpath:/scheduler.properties")
  public static class BlossomSchedulerConfigurationProperties {
    private int maxHistorySize;
    private Duration maxHistoryAge;

    public int getMaxHistorySize() {
      return maxHistorySize;
    }

    public void setMaxHistorySize(int maxHistorySize) {
      this.maxHistorySize = maxHistorySize;
    }

    public Duration getMaxHistoryAge() {
      return maxHistoryAge;
    }

    public void setMaxHistoryAge(Duration maxHistoryAge) {
      this.maxHistoryAge = maxHistoryAge;
    }
  }

  @Bean
  @Qualifier("triggerHistoryCleanJob")
  public JobDetailFactoryBean triggerHistoryCleanJob() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(TriggerHistoryCleanJob.class);
    factoryBean.setGroup("Blossom");
    factoryBean.setName("Quartz trigger history clean");
    factoryBean.setDescription("Remove old trigger history");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  @Qualifier("triggerHistoryCleanCronTrigger")
  public CronTriggerFactoryBean triggerHistoryCleanCronTrigger(
    @Qualifier("triggerHistoryCleanJob") JobDetail triggerHistoryCleanJob) {
    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    factoryBean.setJobDetail(triggerHistoryCleanJob);
    factoryBean.setCronExpression("37 37 * * * ?");
    return factoryBean;
  }


  @Configuration
  @AutoConfigureBefore(QuartzAutoConfiguration.class)
  @EntityScan(basePackageClasses = TriggerHistory.class)
  @EnableJpaRepositories(basePackageClasses = TriggerHistoryRepository.class)
  public static class GlobalListenerQuartzAutoConfiguration {
    @Bean
    public TriggerHistoryDao triggerHistoryDao(TriggerHistoryRepository triggerHistoryRepository,
                                               BlossomSchedulerConfigurationProperties properties) {
      return new TriggerHistoryDaoImpl(triggerHistoryRepository, properties.getMaxHistorySize());
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
