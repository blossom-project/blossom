package com.blossomproject.core.scheduler.supervision;

import com.blossomproject.core.scheduler.job.JobInfo;
import com.blossomproject.core.scheduler.job.ScheduledJobService;
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

  private void mockJob(boolean active, boolean executing, Date nextFireTime) {
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
  public void should_return_up_when_job_inactive() {
    mockJob(false, false, null);
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }

  @Test
  public void should_return_up_when_job_executing() {
    mockJob(true, true, null);
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }

  @Test
  public void should_return_up_when_job_manual() {
    mockJob(true, false, null);
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }

  @Test
  public void should_return_down_when_job_should_have_executed_before() {
    mockJob(true, false, Date.from(Instant.now().minus(1, ChronoUnit.MINUTES)));
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.DOWN, health.getStatus());
  }

  @Test
  public void should_return_up_when_job_will_execute_in_the_future() {
    mockJob(true, false, Date.from(Instant.now().plus(1, ChronoUnit.MINUTES)));
    Health health = indicator.health();
    assertNotNull(health);
    assertEquals(Status.UP, health.getStatus());
  }
}