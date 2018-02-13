package com.blossomproject.core.role;


import com.blossomproject.core.common.search.IndexationEngine;
import com.blossomproject.core.scheduler.IndexationJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class RoleIndexationJob extends IndexationJob {
  @Autowired
  @Qualifier("roleIndexationEngine")
  IndexationEngine roleIndexationEngine;

  @Override
  protected IndexationEngine getIndexationEngine() {
    return roleIndexationEngine;
  }
}
