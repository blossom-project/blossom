package fr.mgargadennec.blossom.core.scheduler.job;

import com.google.common.collect.Lists;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.impl.matchers.GroupMatcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
public class ScheduledJobServiceImpl {
  private final static Logger LOGGER = LoggerFactory.getLogger(ScheduledJobServiceImpl.class);
  private final Scheduler scheduler;

  public ScheduledJobServiceImpl(Scheduler scheduler) {
    this.scheduler = scheduler;
  }

  public Map<String, JobInfo> getAll() {
    try {
      return this.scheduler.getJobKeys(GroupMatcher.groupEquals(groupName)).stream().map(jobKey -> this.getOne(jobKey)).collect(Collectors.toMap());
    } catch (SchedulerException e) {
      LOGGER.error("Cannot retrieve job infos for groupName " + groupName, e);
      return Lists.newArrayList();
    }
  }

  public JobInfo getOne(JobKey jobKey) {
    try {
      List<? extends Trigger> triggers = this.scheduler.getTriggersOfJob(jobKey);

      JobInfo jobInfo = new JobInfo(jobKey);
      jobInfo.setDetail(this.scheduler.getJobDetail(jobKey));
      jobInfo.setAllTriggers(triggers);
      jobInfo.setNextTrigger(triggers.isEmpty() ? null : triggers.get(0));

      return jobInfo;
    } catch (SchedulerException e) {
      LOGGER.error("Cannot retrieve job infos for jobKey " + jobKey, e);
      return null;
    }
  }
}


