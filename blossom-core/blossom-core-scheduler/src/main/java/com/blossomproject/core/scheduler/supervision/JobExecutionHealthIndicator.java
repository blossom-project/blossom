package com.blossomproject.core.scheduler.supervision;

import com.blossomproject.core.scheduler.job.JobInfo;
import com.blossomproject.core.scheduler.job.ScheduledJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthAggregator;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.OrderedHealthAggregator;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class JobExecutionHealthIndicator implements HealthIndicator {

  private final Logger logger = LoggerFactory.getLogger(JobExecutionHealthIndicator.class);

  private final ScheduledJobService jobService;
  private final HealthAggregator aggregator;

  public JobExecutionHealthIndicator(ScheduledJobService jobService) {
    this.jobService = jobService;
    aggregator = new OrderedHealthAggregator();
  }

  @Override
  public Health health() {
    Map<String, Health> groupHealthMap = new HashMap<>();

    for (String group : jobService.getGroups()) {
      groupHealthMap.put(group.replaceAll(" ", ""), healthForGroup(group));
    }

    return aggregator.aggregate(groupHealthMap);
  }

  private Health healthForGroup(String group) {
    Map<String, Health> taskHealthMap = new HashMap<>();

    for (JobInfo jobInfo : jobService.getAll(group)) {
      taskHealthMap.put(jobInfo.getKey().getName().replaceAll(" ", ""), healthForTask(jobInfo));
    }

    return aggregator.aggregate(taskHealthMap);
  }

  private Health healthForTask(JobInfo jobInfo) {
    if (jobInfo.isActive() && !jobInfo.isExecuting() && jobInfo.getNextFireTime() != null
        && jobInfo.getNextFireTime().toInstant().isBefore(Instant.now())) {
      return Health.down().build();
    } else {
      return Health.up().build();
    }
  }
}
