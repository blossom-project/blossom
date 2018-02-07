package com.blossom_project.core.role;


import com.blossom_project.core.common.search.IndexationEngine;
import com.blossom_project.core.scheduler.IndexationJob;
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
