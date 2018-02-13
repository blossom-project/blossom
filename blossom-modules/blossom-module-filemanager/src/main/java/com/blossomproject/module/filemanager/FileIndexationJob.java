package com.blossomproject.module.filemanager;

import com.blossomproject.core.common.search.IndexationEngine;
import com.blossomproject.core.scheduler.IndexationJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

public class FileIndexationJob extends IndexationJob {
  @Autowired
  @Qualifier("fileIndexationEngine")
  IndexationEngine fileIndexationEngine;

  @Override
  protected IndexationEngine getIndexationEngine() {
    return fileIndexationEngine;
  }
}
