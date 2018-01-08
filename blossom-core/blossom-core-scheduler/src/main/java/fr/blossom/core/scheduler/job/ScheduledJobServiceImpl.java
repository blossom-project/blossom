package fr.blossom.core.scheduler.job;

import static org.quartz.impl.matchers.GroupMatcher.groupEquals;

import com.google.common.base.Preconditions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import fr.blossom.core.scheduler.history.TriggerHistory;
import fr.blossom.core.scheduler.history.TriggerHistoryDao;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
public class ScheduledJobServiceImpl implements ScheduledJobService {

  private final static Logger LOGGER = LoggerFactory.getLogger(ScheduledJobServiceImpl.class);
  private final Scheduler scheduler;
  private final TriggerHistoryDao triggerHistoryDao;

  public ScheduledJobServiceImpl(Scheduler scheduler, TriggerHistoryDao triggerHistoryDao) {
    Preconditions.checkNotNull(scheduler);
    Preconditions.checkNotNull(triggerHistoryDao);
    this.scheduler = scheduler;
    this.triggerHistoryDao = triggerHistoryDao;
  }

  @Override
  public void changeState(boolean activate) {
    try {
      if (this.scheduler.isStarted()) {
        if (activate) {
          this.scheduler.start();
        } else {
          this.scheduler.standby();
        }
      }
    } catch (SchedulerException e) {
      LOGGER.error("Cannot change scheduler state", e);
    }
  }

  @Override
  public List<String> getGroups() {
    try {
      return this.scheduler.getJobGroupNames();
    } catch (SchedulerException e) {
      LOGGER.error("Cannot retrieve job group names from quartz scheduler", e);
      return Lists.newArrayList();
    }
  }


  @Override
  public SchedulerInfo getSchedulerInfo() {
    SchedulerInfo schedulerInfo = new SchedulerInfo();
    try {
      schedulerInfo.setName(this.scheduler.getMetaData().getSchedulerName());
      schedulerInfo.setStart(this.scheduler.getMetaData().getRunningSince());
      schedulerInfo.setPoolsize(this.scheduler.getMetaData().getThreadPoolSize());
      schedulerInfo.setStarted(this.scheduler.getMetaData().isStarted());
      schedulerInfo.setStandBy(this.scheduler.getMetaData().isInStandbyMode());
      schedulerInfo.setJobs(this.computeJobCount());
      schedulerInfo.setTriggers(this.computeTriggerCount());
    } catch (SchedulerException e) {
      LOGGER.error("Cannot retrieve scheduler info.", e);
    }

    return schedulerInfo;

  }

  @Override
  public List<JobInfo> getAll(String groupName) {
    try {
      List<JobInfo> jobInfos = Lists.newArrayList();
      Set<JobKey> jobKeys = this.scheduler.getJobKeys(groupEquals(groupName));
      for (JobKey jobKey : jobKeys) {
        JobInfo jobInfo = this.getOne(jobKey);
        if (jobInfo != null) {
          jobInfos.add(jobInfo);
        }
      }
      return jobInfos;
    } catch (SchedulerException e) {
      LOGGER.error("Cannot retrieve job infos for groupName " + groupName, e);
      return Lists.newArrayList();
    }
  }

  @Override
  public JobInfo getOne(JobKey jobKey) {
    try {
      JobDetail jobDetail = this.scheduler.getJobDetail(jobKey);
      List<? extends Trigger> triggers = this.scheduler.getTriggersOfJob(jobKey);
      List<JobExecutionContext> jobExecutionContexts = this.scheduler.getCurrentlyExecutingJobs()
        .stream().filter(jec -> jec.getJobDetail().getKey().equals(jobKey))
        .collect(Collectors.toList());

      JobInfo jobInfo = new JobInfo(jobKey);
      jobInfo.setDetail(jobDetail);
      jobInfo.setTriggers(triggers);
      jobInfo.setJobExecutionContexts(jobExecutionContexts);

      List<TriggerHistory> triggerHistories = triggerHistoryDao.getJobHistory(jobKey);
      jobInfo.setLastExecutedTrigger(Iterables.getFirst(triggerHistories, null));
      jobInfo.setHistory(triggerHistories);

      return jobInfo;
    } catch (SchedulerException e) {
      LOGGER.error("Cannot retrieve job infos for jobKey " + jobKey, e);
      return null;
    }
  }

  @Override
  public void execute(JobKey jobKey) {
    try {
      scheduler.triggerJob(jobKey);
    } catch (SchedulerException e) {
      LOGGER.error("Cannot execute job for jobKey " + jobKey, e);
    }
  }

  protected long computeTriggerCount() {
    try {
      return this.scheduler.getTriggerGroupNames().stream().flatMap(groupName -> {
        try {
          return this.scheduler.getTriggerKeys(groupEquals(groupName)).stream();
        } catch (SchedulerException e) {
          return Stream.of();
        }
      }).count();
    } catch (SchedulerException e) {
      LOGGER.error("Cannot count triggers", e);
      return 0;
    }
  }

  protected long computeJobCount() {
    try {
      return this.scheduler.getJobGroupNames().stream().flatMap(groupName -> {
        try {
          return this.scheduler.getJobKeys(groupEquals(groupName)).stream();
        } catch (SchedulerException e) {
          return Stream.of();
        }
      }).count();
    } catch (SchedulerException e) {
      LOGGER.error("Cannot count jobs", e);
      return 0;
    }
  }

}


