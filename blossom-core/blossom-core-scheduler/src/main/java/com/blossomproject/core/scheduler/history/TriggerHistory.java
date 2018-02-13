package com.blossomproject.core.scheduler.history;

import com.blossomproject.core.common.entity.AbstractEntity;
import java.sql.Timestamp;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

public class TriggerHistory extends AbstractEntity {

  private TriggerKey triggerKey;
  private JobKey jobKey;
  private String fireInstanceId;
  private Timestamp startTime;
  private Timestamp endTime;

  public TriggerKey getTriggerKey() {
    return triggerKey;
  }

  public void setTriggerKey(TriggerKey triggerKey) {
    this.triggerKey = triggerKey;
  }

  public JobKey getJobKey() {
    return jobKey;
  }

  public void setJobKey(JobKey jobKey) {
    this.jobKey = jobKey;
  }

  public String getFireInstanceId() {
    return fireInstanceId;
  }

  public void setFireInstanceId(String fireInstanceId) {
    this.fireInstanceId = fireInstanceId;
  }

  public Timestamp getStartTime() {
    return startTime;
  }

  public void setStartTime(Timestamp startTime) {
    this.startTime = startTime;
  }

  public Timestamp getEndTime() {
    return endTime;
  }

  public void setEndTime(Timestamp endTime) {
    this.endTime = endTime;
  }
}
