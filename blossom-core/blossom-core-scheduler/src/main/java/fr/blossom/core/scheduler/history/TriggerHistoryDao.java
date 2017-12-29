package fr.blossom.core.scheduler.history;

import java.util.List;
import org.quartz.JobKey;

public interface TriggerHistoryDao {

  List<TriggerHistory> getJobHistory(JobKey jobKey);

  void create(TriggerHistory triggerHistory);

  void updateEndDate(String fireInstanceId);
}
