package com.blossomproject.core.scheduler.history;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TriggerHistoryCleanJob implements Job {

  private final TriggerHistoryRepository triggerHistoryRepository;
  private final Duration maxAge;

  public TriggerHistoryCleanJob(TriggerHistoryRepository triggerHistoryRepository,
                                @Value("${blossom.scheduler.max-history-age}") Duration maxAge) {
    this.triggerHistoryRepository = triggerHistoryRepository;
    this.maxAge = maxAge;
  }

  @Override
  @Transactional
  public void execute(JobExecutionContext context) throws JobExecutionException {
    Timestamp oldestStartTime = new Timestamp(Instant.now().minus(maxAge.getSeconds(), ChronoUnit.SECONDS).getEpochSecond() * 1000);
    triggerHistoryRepository.deleteAllByStartTimeBefore(oldestStartTime);
  }

}
