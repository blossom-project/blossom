package fr.blossom.core.scheduler.job;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import fr.blossom.core.scheduler.history.TriggerHistoryDao;
import java.util.Date;
import java.util.List;
import java.util.Set;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.TriggerKey;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.matchers.GroupMatcher;

@RunWith(MockitoJUnitRunner.class)
public class ScheduledJobServiceImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private ScheduledJobServiceImpl scheduledJobService;
  private TriggerHistoryDao triggerHistoryDao;
  private Scheduler scheduler;

  @Before
  public void setUp() throws Exception {
    this.scheduler = mock(Scheduler.class);
    this.triggerHistoryDao = mock(TriggerHistoryDao.class);
    this.scheduledJobService = new ScheduledJobServiceImpl(this.scheduler, this.triggerHistoryDao);
  }

  @Test
  public void should_succeed_instanciate() throws Exception {
    new ScheduledJobServiceImpl(this.scheduler, this.triggerHistoryDao);
  }

  @Test
  public void should_fail_instanciation_when_null_scheduler() throws Exception {
    thrown.expect(NullPointerException.class);
    new ScheduledJobServiceImpl(null, this.triggerHistoryDao);
  }

  @Test
  public void should_fail_instanciation_when_null_triggerhistorydao() throws Exception {
    thrown.expect(NullPointerException.class);
    new ScheduledJobServiceImpl(this.scheduler, null);
  }

  @Test
  public void should_change_state_to_started_when_already_started() throws Exception {
    when(this.scheduler.isStarted()).thenReturn(true);
    this.scheduledJobService.changeState(true);
  }

  @Test
  public void should_change_state_to_standby_when_started() throws Exception {
    when(this.scheduler.isStarted()).thenReturn(true);
    this.scheduledJobService.changeState(false);
  }

  @Test
  public void should_change_state_to_standby_when_not_started() throws Exception {
    when(this.scheduler.isStarted()).thenReturn(false);
    this.scheduledJobService.changeState(false);
  }

  @Test
  public void should_change_state_to_started_when_not_started() throws Exception {
    when(this.scheduler.isStarted()).thenReturn(false);
    this.scheduledJobService.changeState(true);
  }

  @Test
  public void should_change_state_catch_exception() throws Exception {
    when(this.scheduler.isStarted()).thenThrow(new SchedulerException());
    this.scheduledJobService.changeState(true);
  }


  @Test
  public void should_get_group_names() throws Exception {
    List<String> groupNames = Lists.newArrayList("test", "test2", "test3");
    when(this.scheduler.getJobGroupNames()).thenReturn(groupNames);
    List<String> result = this.scheduledJobService.getGroups();
    assertEquals(groupNames, result);
  }

  @Test
  public void should_get_group_catch_exception() throws Exception {
    when(this.scheduler.getJobGroupNames()).thenThrow(new SchedulerException());
    List<String> result = this.scheduledJobService.getGroups();
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void should_get_scheduler_info() throws Exception {
    SchedulerMetaData metaData = mock(SchedulerMetaData.class);
    when(metaData.getThreadPoolSize()).thenReturn(5);
    when(metaData.getRunningSince()).thenReturn(new Date());
    when(metaData.isStarted()).thenReturn(true);
    when(metaData.getSchedulerName()).thenReturn("schedulerName");
    when(metaData.getSchedulerInstanceId()).thenReturn("schedulerInstanceId");

    when(this.scheduler.getMetaData()).thenReturn(metaData);

    SchedulerInfo schedulerInfo = this.scheduledJobService.getSchedulerInfo();

    assertNotNull(schedulerInfo);
    assertEquals(schedulerInfo.getName(), metaData.getSchedulerName());
    assertEquals(schedulerInfo.getPoolsize(), metaData.getThreadPoolSize());
    assertEquals(schedulerInfo.getStart(), metaData.getRunningSince());
    assertEquals(schedulerInfo.isStandBy(), metaData.isInStandbyMode());
    assertEquals(schedulerInfo.isStarted(), metaData.isStarted());
    assertEquals(schedulerInfo.getJobs(), 0);
    assertEquals(schedulerInfo.getTriggers(), 0);
  }

  @Test
  public void should_get_scheduler_info_catch_info() throws Exception {
    when(this.scheduler.getMetaData()).thenThrow(new SchedulerException());
    SchedulerInfo schedulerInfo = this.scheduledJobService.getSchedulerInfo();
    assertNotNull(schedulerInfo);
    assertNull(schedulerInfo.getName());
    assertEquals(0, schedulerInfo.getPoolsize());
    assertNull(schedulerInfo.getStart());
    assertEquals(0, schedulerInfo.getJobs());
    assertEquals(0, schedulerInfo.getTriggers());
  }

  @Test
  public void should_get_triggers() throws Exception {
    String groupName = "test";
    List<String> triggerGroups = Lists.newArrayList(groupName);
    Set<TriggerKey> triggerKeys = Sets.newHashSet(new TriggerKey("trigger", groupName));

    when(this.scheduler.getTriggerGroupNames()).thenReturn(triggerGroups);
    when(this.scheduler.getTriggerKeys(groupEquals(groupName))).thenReturn(triggerKeys);

    long triggerCount = this.scheduledJobService.computeTriggerCount();
    assertEquals(triggerCount, 1l);
  }

  @Test
  public void should_get_triggers_catch_exception_in_lambda() throws Exception {
    String groupName = "test";
    List<String> triggerGroups = Lists.newArrayList(groupName);
    when(this.scheduler.getTriggerGroupNames()).thenReturn(triggerGroups);
    when(this.scheduler.getTriggerKeys(any(GroupMatcher.class)))
      .thenThrow(new SchedulerException());
    long triggerCount = this.scheduledJobService.computeTriggerCount();
    assertEquals(triggerCount, 0l);
  }

  @Test
  public void should_get_triggers_catch_exception() throws Exception {
    when(this.scheduler.getTriggerGroupNames()).thenThrow(new SchedulerException());
    long triggerCount = this.scheduledJobService.computeTriggerCount();
    assertEquals(triggerCount, 0l);
  }


  @Test
  public void should_get_jobs() throws Exception {
    String groupName = "test";
    List<String> jobGroups = Lists.newArrayList(groupName);
    Set<JobKey> jobKeys = Sets.newHashSet(new JobKey("job", groupName));

    when(this.scheduler.getJobGroupNames()).thenReturn(jobGroups);
    when(this.scheduler.getJobKeys(groupEquals(groupName))).thenReturn(jobKeys);

    long jobCount = this.scheduledJobService.computeJobCount();
    assertEquals(jobCount, 1l);
  }

  @Test
  public void should_get_jobs_catch_exception_in_lambda() throws Exception {
    String groupName = "test";
    List<String> jobGroups = Lists.newArrayList(groupName);
    when(this.scheduler.getJobGroupNames()).thenReturn(jobGroups);
    when(this.scheduler.getJobKeys(any(GroupMatcher.class))).thenThrow(new SchedulerException());

    long jobCount = this.scheduledJobService.computeJobCount();
    assertEquals(jobCount, 0l);
  }

  @Test
  public void should_get_jobs_catch_exception() throws Exception {
    when(this.scheduler.getJobGroupNames()).thenThrow(new SchedulerException());
    long triggerCount = this.scheduledJobService.computeJobCount();
    assertEquals(triggerCount, 0l);
  }

  @Test
  public void should_get_all_jobs_infos() throws Exception {
    String groupName = "test";
    when(this.scheduler.getJobKeys(any(GroupMatcher.class)))
      .thenReturn(Sets.newHashSet(new JobKey("test", groupName), new JobKey("test2", groupName)));
    when(this.scheduler.getJobDetail(any(JobKey.class))).thenReturn(new JobDetailImpl());
    List<JobInfo> result = this.scheduledJobService.getAll(groupName);

    assertNotNull(result);
    assertFalse(result.isEmpty());
    assertTrue(result.size() == 2);
  }

  @Test
  public void should_get_all_jobs_catch_exception() throws Exception {
    String groupName = "test";
    when(this.scheduler.getJobKeys(any(GroupMatcher.class))).thenThrow(new SchedulerException());

    List<JobInfo> result = this.scheduledJobService.getAll(groupName);
    assertNotNull(result);
    assertTrue(result.isEmpty());
  }

  @Test
  public void should_execute() throws Exception {
    JobKey jobKey = new JobKey("any", "job");
    this.scheduledJobService.execute(jobKey);
    verify(this.scheduler, times(1)).triggerJob(jobKey);
  }

  @Test
  public void should_execute_with_exception() throws Exception {
    JobKey jobKey = new JobKey("any", "job");
    doThrow(new SchedulerException()).when(this.scheduler).triggerJob(eq(jobKey));
    this.scheduledJobService.execute(jobKey);
  }


  @Test
  public void should_get_one_job_infos() throws Exception {
    when(this.scheduler.getJobDetail(any(JobKey.class))).thenAnswer(a -> {
      JobDetailImpl jobDetail = new JobDetailImpl();
      jobDetail.setName("test");
      jobDetail.setDescription("test");
      jobDetail.setGroup("test");
      jobDetail.setKey(a.getArgumentAt(0, JobKey.class));
      jobDetail.setRequestsRecovery(true);
      jobDetail.setDurability(true);
      return jobDetail;
    });

    JobInfo result = this.scheduledJobService.getOne(new JobKey("test","test"));

    assertNotNull(result);
  }


  @Test
  public void should_get_one_job_catch_exception() throws Exception {
    when(this.scheduler.getJobDetail(any(JobKey.class))).thenThrow(new SchedulerException());
    JobInfo result = this.scheduledJobService.getOne(new JobKey("test","test"));
    assertNull(result);
  }
}
