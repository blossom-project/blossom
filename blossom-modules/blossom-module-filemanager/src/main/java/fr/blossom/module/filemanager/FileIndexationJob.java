package fr.blossom.module.filemanager;

import fr.blossom.core.common.search.IndexationEngine;
import fr.blossom.core.scheduler.IndexationJob;
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
