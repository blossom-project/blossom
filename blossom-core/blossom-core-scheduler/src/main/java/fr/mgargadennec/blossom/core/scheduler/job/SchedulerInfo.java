package fr.mgargadennec.blossom.core.scheduler.job;

import java.util.Date;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
public class SchedulerInfo {
  private String name;
  private Date start;
  private int poolsize;
  private long jobs;
  private long triggers;
  private boolean started;
  private boolean standBy;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Date getStart() {
    return start;
  }

  public void setStart(Date start) {
    this.start = start;
  }

  public int getPoolsize() {
    return poolsize;
  }

  public void setPoolsize(int poolsize) {
    this.poolsize = poolsize;
  }

  public long getJobs() {
    return jobs;
  }

  public void setJobs(long jobs) {
    this.jobs = jobs;
  }

  public long getTriggers() {
    return triggers;
  }

  public void setTriggers(long triggers) {
    this.triggers = triggers;
  }

  public boolean isStarted() {
    return started;
  }

  public void setStarted(boolean started) {
    this.started = started;
  }

  public boolean isStandBy() {
    return standBy;
  }

  public void setStandBy(boolean standBy) {
    this.standBy = standBy;
  }
}
