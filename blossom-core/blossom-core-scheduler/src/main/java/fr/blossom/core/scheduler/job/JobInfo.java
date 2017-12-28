package fr.blossom.core.scheduler.job;

import fr.blossom.core.scheduler.history.TriggerHistory;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.Date;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
public class JobInfo {
  private JobKey key;
  private JobDetail detail;
  private TriggerHistory lastExecutedTrigger;
  private List<? extends Trigger> triggers;
  private List<JobExecutionContext> jobExecutionContexts;

  public JobInfo(JobKey key) {
    this.key = key;
  }

  public JobKey getKey() {
    return key;
  }

  public JobDetail getDetail() {
    return detail;
  }

  public void setDetail(JobDetail detail) {
    this.detail = detail;
  }

  public TriggerHistory getLastExecutedTrigger() {
    return lastExecutedTrigger;
  }

  public void setLastExecutedTrigger(TriggerHistory lastExecutedTrigger) {
    this.lastExecutedTrigger = lastExecutedTrigger;
  }

  public List<? extends Trigger> getTriggers() {
    return triggers;
  }

  public void setTriggers(List<? extends Trigger> triggers) {
    this.triggers = triggers;
  }

  public List<JobExecutionContext> getJobExecutionContexts() {
    return jobExecutionContexts;
  }

  public void setJobExecutionContexts(List<JobExecutionContext> jobExecutionContexts) {
    this.jobExecutionContexts = jobExecutionContexts;
  }

  public Date getPreviousFireTime() {
    return this.triggers.stream().filter(t-> t.getPreviousFireTime()!=null).map(t -> t.getPreviousFireTime()).max(Date::compareTo).orElse(null);
  }

  public Date getNextFireTime() {
    return this.triggers.stream().filter(t-> t.getNextFireTime()!=null).map(t -> t.getNextFireTime()).min(Date::compareTo).orElse(null);
  }

  public boolean isActive() {
    return this.triggers.stream().anyMatch(t -> t.getNextFireTime()!=null);
  }

  public boolean isExecuting(){
    return jobExecutionContexts.stream().anyMatch(exec -> triggers.contains(exec.getTrigger()));
  }
  public boolean isExecuting(Trigger trigger){
    return jobExecutionContexts.stream().anyMatch(exec -> exec.getTrigger().equals(trigger));
  }
}
