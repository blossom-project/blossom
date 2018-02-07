package com.blossom_project.core.group;


import com.blossom_project.core.common.search.IndexationEngine;
import com.blossom_project.core.scheduler.IndexationJob;
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
