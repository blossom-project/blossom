package fr.blossom.core.scheduler.history;

import java.sql.Timestamp;
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
      .setJobKey(new JobKey(rs.getString("job_group"), rs.getString("job_name")));
    triggerHistory
      .setTriggerKey(new TriggerKey(rs.getString("trigger_group"), rs.getString("trigger_name")));
    return triggerHistory;
  };
  private final JdbcTemplate jdbcTemplate;

  public TriggerHistoryDaoImpl(JdbcTemplate jdbcTemplate) {
    this.jdbcTemplate = jdbcTemplate;
  }

  @Override
  public TriggerHistory getLastForJob(JobKey jobKey) {
    try {
      List<TriggerHistory> history = jdbcTemplate.query(
        "select * from " + TABLE_NAME
          + " where job_group=? and job_name=? order by start_time desc",
        new Object[]{jobKey.getGroup(), jobKey.getName()},
        mapper);

      if(history.isEmpty()){
        return null;
      }
      return history.get(0);

    }catch(Exception e){
      logger.error("Cannot get last TriggerHistory",e);
      return null;
    }

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
