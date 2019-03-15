package com.blossomproject.core.scheduler.history;

import org.quartz.JobKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class TriggerHistoryDaoImpl implements TriggerHistoryDao {

  private final static Logger logger = LoggerFactory.getLogger(TriggerHistoryDaoImpl.class);

  private final TriggerHistoryRepository repository;
  private final Integer maxHistorySize;

  public TriggerHistoryDaoImpl(TriggerHistoryRepository repository, Integer maxHistorySize) {
    this.repository = repository;
    this.maxHistorySize = maxHistorySize;
  }

  @Override
  @Transactional
  public List<TriggerHistory> getJobHistory(JobKey jobKey) {
    try {
      List<TriggerHistory> histories = repository.getByJobNameAndJobGroup(
        jobKey.getName(),
        jobKey.getGroup(),
        PageRequest.of(0, maxHistorySize + 1, Sort.Direction.DESC, "startTime")
      );

      if (histories.isEmpty()) {
        return Collections.emptyList();
      }

      return histories;

    } catch (Exception e) {
      logger.error("Cannot get last TriggerHistory", e);
      return Collections.emptyList();
    }
  }

  @Override
  @Transactional
  public void create(TriggerHistory triggerHistory) {
    triggerHistory.setCreationUser("scheduler");
    triggerHistory.setModificationUser("scheduler");
    repository.save(triggerHistory);
  }

  @Override
  @Transactional
  public void updateEndDate(String fireInstanceId) {
    TriggerHistory triggerHistory = repository.getFirstByFireInstanceId(fireInstanceId);
    triggerHistory.setEndTime(new Timestamp(System.currentTimeMillis()));
    repository.save(triggerHistory);
  }
}
