package fr.mgargadennec.blossom.core.scheduler.job;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Trigger;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
public class JobInfo {
  private JobKey key;
  private JobDetail detail;
  private Trigger nextTrigger;
  private List<? extends Trigger> allTriggers;

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

  public Trigger getNextTrigger() {
    return nextTrigger;
  }

  public void setNextTrigger(Trigger nextTrigger) {
    this.nextTrigger = nextTrigger;
  }

  public List<? extends Trigger> getAllTriggers() {
    return allTriggers;
  }

  public void setAllTriggers(List<? extends Trigger> allTriggers) {
    this.allTriggers = allTriggers;
  }
}
