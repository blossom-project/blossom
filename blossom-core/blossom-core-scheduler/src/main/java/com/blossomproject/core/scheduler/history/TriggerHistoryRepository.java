package com.blossomproject.core.scheduler.history;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public interface TriggerHistoryRepository extends JpaRepository<TriggerHistory, Long> {

  List<TriggerHistory> getByJobNameAndJobGroup(String jobName, String jobGroup, Pageable page);

  void deleteAllByStartTimeBefore(Timestamp date);

  TriggerHistory getFirstByFireInstanceId(String fireInstanceId);

}
