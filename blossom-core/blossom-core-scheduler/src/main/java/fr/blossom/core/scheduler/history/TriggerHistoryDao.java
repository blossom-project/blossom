package fr.blossom.core.scheduler.history;

import org.quartz.JobKey;

public interface TriggerHistoryDao {

  TriggerHistory getLastForJob(JobKey jobKey);

  void create(TriggerHistory triggerHistory);

  void updateEndDate(String fireInstanceId);
}
