package com.blossom_project.module.filemanager;

import com.blossom_project.core.common.search.IndexationEngine;
import com.blossom_project.core.scheduler.IndexationJob;
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
