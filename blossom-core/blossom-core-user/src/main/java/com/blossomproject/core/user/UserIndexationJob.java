package com.blossomproject.core.user;


import com.blossomproject.core.common.search.IndexationEngine;
import com.blossomproject.core.scheduler.IndexationJob;
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
