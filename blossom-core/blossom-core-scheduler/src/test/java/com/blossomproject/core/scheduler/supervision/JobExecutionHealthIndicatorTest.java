package com.blossomproject.core.scheduler.supervision;

import com.blossomproject.core.scheduler.job.JobInfo;
import com.blossomproject.core.scheduler.job.ScheduledJobService;
import com.blossomproject.core.scheduler.job.SchedulerInfo;
import org.junit.Before;
import org.junit.Test;
import org.quartz.JobKey;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.Status;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

public class JobExecutionHealthIndicatorTest {

  private JobExecutionHealthIndicator indicator;
  private ScheduledJobService jobService;

  @Before
  public void prepare() {
    jobService = mock(ScheduledJobService.class);
    indicator = new JobExecutionHealthIndicator(jobService);
  }

  private void mockJob(boolean schedulerStarted, boolean schedulerStandby, boolean active,
                       boolean executing, Date nextFireTime) {
    SchedulerInfo schedulerInfo = mock(SchedulerInfo.class);
    doReturn(schedulerStarted).when(schedulerInfo).isStarted();
    doReturn(schedulerStandby).when(schedulerInfo).isStandBy();
    doReturn(schedulerInfo).when(jobService).getSchedulerInfo();

    JobInfo jobInfo = mock(JobInfo.class);
    doReturn(new JobKey("test")).when(jobInfo).getKey();
    doReturn(active).when(jobInfo).isActive();
    doReturn(executing).when(jobInfo).isExecuting();
    doReturn(nextFireTime).when(jobInfo).getNextFireTime();

    doReturn(Collections.singletonList(jobInfo)).when(jobService).getAll(anyString());
    doReturn(jobInfo).when(jobService).getOne(any());
    doReturn(Collections.singletonList("test")).when(jobService).getGroups();
  }

  @Test
  public void should_return_up_when_scheduler_not_started() {
    mockJob(false, false, false, false, null);
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }

  @Test
  public void should_return_up_when_scheduler_standby() {
    mockJob(true, true, false, false, null);
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }

  @Test
  public void should_return_up_when_job_inactive() {
    mockJob(true, false, false, false, null);
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }

  @Test
  public void should_return_up_when_job_executing() {
    mockJob(true, false, true, true, null);
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }

  @Test
  public void should_return_up_when_job_manual() {
    mockJob(true, false, true, false, null);
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }

  @Test
  public void should_return_down_when_job_should_have_executed_before() {
    mockJob(true, false, true, false, Date.from(Instant.now().minus(1, ChronoUnit.MINUTES)));
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.DOWN, health.getStatus());
  }

  @Test
  public void should_return_up_when_job_will_execute_in_the_future() {
    mockJob(true, false, true, false, Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)));
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }
}