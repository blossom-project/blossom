package com.blossom_project.core.user;


import com.blossom_project.core.common.search.IndexationEngine;
import com.blossom_project.core.scheduler.IndexationJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class UserIndexationJob extends IndexationJob {
  @Autowired
  @Qualifier("userIndexationEngine")
  IndexationEngine userDTOIndexationEngine;

  @Override
  protected IndexationEngine getIndexationEngine() {
    return userDTOIndexationEngine;
  }
}
