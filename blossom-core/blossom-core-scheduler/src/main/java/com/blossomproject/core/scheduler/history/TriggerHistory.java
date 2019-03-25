package com.blossomproject.core.scheduler.history;

import com.blossomproject.core.common.entity.AbstractEntity;
import org.quartz.JobKey;
import org.quartz.TriggerKey;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.sql.Timestamp;

@Entity
@Table(name = "qrtz_trigger_history")
public class TriggerHistory extends AbstractEntity {

  @Column(name = "trigger_name")
  private String triggerName;
  @Column(name = "trigger_group")
  private String triggerGroup;
  @Column(name = "job_name")
  private String jobName;
  @Column(name = "job_group")
  private String jobGroup;
  @Column(name = "fire_instance_id")
  private String fireInstanceId;
  @Column(name = "start_time")
  private Timestamp startTime;
  @Column(name = "end_time")
  private Timestamp endTime;

  public TriggerKey getTriggerKey() {
    return new TriggerKey(triggerName, triggerGroup);
  }

  @Transient
  public void setTriggerKey(TriggerKey triggerKey) {
    this.triggerName = triggerKey.getName();
    this.triggerGroup = triggerKey.getGroup();
  }

  public String getTriggerName() {
    return triggerName;
  }

  public void setTriggerName(String triggerName) {
    this.triggerName = triggerName;
  }

  public String getTriggerGroup() {
    return triggerGroup;
  }

  public void setTriggerGroup(String triggerGroup) {
    this.triggerGroup = triggerGroup;
  }

  public JobKey getJobKey() {
    return new JobKey(jobName, jobGroup);
  }

  @Transient
  public void setJobKey(JobKey jobKey) {
    this.jobGroup = jobKey.getGroup();
    this.jobName = jobKey.getName();
  }

  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  public String getJobGroup() {
    return jobGroup;
  }

  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
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
