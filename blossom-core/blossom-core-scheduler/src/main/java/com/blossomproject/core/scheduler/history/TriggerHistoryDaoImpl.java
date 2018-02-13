package com.blossomproject.core.scheduler.history;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import org.quartz.JobKey;
import org.quartz.TriggerKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class TriggerHistoryDaoImpl implements TriggerHistoryDao {

  private final static Logger logger = LoggerFactory.getLogger(TriggerHistoryDaoImpl.class);

  private final static String TABLE_NAME = "qrtz_trigger_history";
  private final static RowMapper<TriggerHistory> mapper = (rs, rowNum) -> {
    TriggerHistory triggerHistory = new TriggerHistory();
    triggerHistory.setId(rs.getLong("id"));
    triggerHistory.setCreationDate(rs.getDate("creation_date"));
    triggerHistory.setCreationUser(rs.getString("creation_user"));
    triggerHistory.setModificationDate(rs.getDate("modification_date"));
    triggerHistory.setModificationUser(rs.getString("modification_user"));
    triggerHistory.setFireInstanceId(rs.getString("fire_instance_id"));
    triggerHistory.setStartTime(rs.getTimestamp("start_time"));
    triggerHistory.setEndTime(rs.getTimestamp("end_time"));
    triggerHistory
      .setJobKey(new JobKey(rs.getString("job_name"), rs.getString("job_group")));
    triggerHistory
      .setTriggerKey(new TriggerKey(rs.getString("trigger_name"), rs.getString("trigger_group")));
    return triggerHistory;
  };
  private final JdbcTemplate jdbcTemplate;
  private final Integer maxHistorySize;

  public TriggerHistoryDaoImpl(JdbcTemplate jdbcTemplate, Integer maxHistorySize) {
    this.jdbcTemplate = jdbcTemplate;
    this.maxHistorySize = maxHistorySize;
  }

  @Override
  public List<TriggerHistory> getJobHistory(JobKey jobKey) {
    List<TriggerHistory> history = null;
    try {
      List<TriggerHistory> histories = jdbcTemplate.query(
        "select * from " + TABLE_NAME
          + " where job_group=? and job_name=? order by start_time desc",
        new Object[]{jobKey.getGroup(), jobKey.getName()},
        mapper);

      if (histories.isEmpty()) {
        return new ArrayList<>();
      }

      if (histories.size() > this.maxHistorySize) {
        this.cleanHistory(jobKey, histories.get(this.maxHistorySize).getStartTime());
        history = histories.subList(0, this.maxHistorySize);
      } else {
        history = histories;
      }
      return history;
    } catch (Exception e) {
      logger.error("Cannot get last TriggerHistory", e);
      return history;
    }
  }

  private void cleanHistory(JobKey jobKey, Timestamp olderThan) {
    this.jdbcTemplate
      .update("DELETE FROM " + TABLE_NAME + " WHERE job_group=? and job_name=? and start_time <= ?",
        jobKey.getGroup(), jobKey.getName(), olderThan);
  }

  @Override
  public void create(TriggerHistory triggerHistory) {
    Timestamp now = new Timestamp(System.currentTimeMillis());
    jdbcTemplate.update(
      "INSERT INTO " + TABLE_NAME
        + "(id, creation_date, creation_user,modification_date,modification_user, fire_instance_id,start_time,end_time,job_group,job_name,trigger_group,trigger_name) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)",
      triggerHistory.getId(),
      now,
      "scheduler",
      now,
      "scheduler",
      triggerHistory.getFireInstanceId(),
      triggerHistory.getStartTime(),
      triggerHistory.getEndTime(),
      triggerHistory.getJobKey().getGroup(),
      triggerHistory.getJobKey().getName(),
      triggerHistory.getTriggerKey().getGroup(),
      triggerHistory.getTriggerKey().getName()
    );
  }

  @Override
  public void updateEndDate(String fireInstanceId) {
    jdbcTemplate.update(
      "update " + TABLE_NAME + " set end_time=? where fire_instance_id=?",
      new Timestamp(System.currentTimeMillis()),
      fireInstanceId);
  }
}
