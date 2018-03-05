package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.scheduler.history.TriggerHistoryDao;
import com.blossomproject.core.scheduler.history.TriggerHistoryDaoImpl;
import com.blossomproject.core.scheduler.job.ScheduledJobService;
import com.blossomproject.core.scheduler.job.ScheduledJobServiceImpl;
import com.blossomproject.core.scheduler.listener.GlobalTriggerListener;
import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
public class SchedulerAutoConfiguration {

  @Bean
  public TriggerHistoryDao triggerHistoryDao(JdbcTemplate jdbcTemplate) {
    return new TriggerHistoryDaoImpl(jdbcTemplate, 10);
  }

  @Bean
  public GlobalTriggerListener globalTriggerListener(TriggerHistoryDao triggerHistoryDao) {
    return new GlobalTriggerListener(triggerHistoryDao);
  }

  @Bean
  public ScheduledJobService scheduledJobService(Scheduler scheduler,
    TriggerHistoryDao triggerHistoryDao) {
    return new ScheduledJobServiceImpl(scheduler, triggerHistoryDao);
  }

}
