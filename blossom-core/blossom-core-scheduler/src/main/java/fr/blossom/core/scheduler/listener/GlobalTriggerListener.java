package fr.blossom.core.scheduler.listener;

import com.google.common.base.Preconditions;
import fr.blossom.core.scheduler.history.TriggerHistory;
import fr.blossom.core.scheduler.history.TriggerHistoryDao;
import java.sql.Timestamp;
import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GlobalTriggerListener implements TriggerListener {

  private final static Logger logger = LoggerFactory.getLogger(GlobalTriggerListener.class);
  private final static String NAME = "GlobalTriggerListener";
  private final TriggerHistoryDao triggerHistoryDao;

  public GlobalTriggerListener(TriggerHistoryDao triggerHistoryDao) {
    Preconditions.checkArgument(triggerHistoryDao != null);
    this.triggerHistoryDao = triggerHistoryDao;
  }

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public boolean vetoJobExecution(Trigger trigger, JobExecutionContext context) {
    //Never veto anything
    return false;
  }

  @Override
  public void triggerMisfired(Trigger trigger) {
    //Do nothing
  }

  @Override
  public void triggerFired(Trigger trigger, JobExecutionContext context) {
    if(logger.isDebugEnabled()){
      logger.debug("Trigger fired with id {} for triggerKey ({} - {}) for jobKey ({} - {})",
        context.getFireInstanceId(), trigger.getKey().getGroup(), trigger.getKey().getName(),
        trigger.getJobKey().getGroup(), trigger.getJobKey().getName());
    }

    TriggerHistory history = new TriggerHistory();
    history.ensureId();
    history.setFireInstanceId(context.getFireInstanceId());
    history.setJobKey(trigger.getJobKey());
    history.setTriggerKey(trigger.getKey());
    history.setStartTime(new Timestamp(context.getFireTime().getTime()));
    history.setEndTime(null);

    triggerHistoryDao.create(history);
  }

  @Override
  public void triggerComplete(Trigger trigger, JobExecutionContext context,
    CompletedExecutionInstruction triggerInstructionCode) {
    if(logger.isDebugEnabled()) {
      logger
        .debug("Trigger completed with id {} for triggerKey ({} - {}) for jobKey ({} - {})",
          context.getFireInstanceId(), trigger.getKey().getGroup(), trigger.getKey().getName(),
          trigger.getJobKey().getGroup(), trigger.getJobKey().getName());
    }

    String fireInstanceId = context.getFireInstanceId();
    triggerHistoryDao.updateEndDate(fireInstanceId);
  }
}
