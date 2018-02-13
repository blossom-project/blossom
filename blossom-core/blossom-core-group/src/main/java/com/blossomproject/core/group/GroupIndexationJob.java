package com.blossomproject.core.group;


import com.blossomproject.core.common.search.IndexationEngine;
import com.blossomproject.core.scheduler.IndexationJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class GroupIndexationJob extends IndexationJob {
  @Autowired
  @Qualifier("groupIndexationEngine")
  IndexationEngine groupIndexationEngine;

  @Override
  protected IndexationEngine getIndexationEngine() {
    return groupIndexationEngine;
  }
}
