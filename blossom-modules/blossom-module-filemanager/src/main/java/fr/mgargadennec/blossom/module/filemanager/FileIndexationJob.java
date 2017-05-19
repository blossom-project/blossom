package fr.mgargadennec.blossom.module.filemanager;

import fr.mgargadennec.blossom.core.common.search.IndexationEngine;
import fr.mgargadennec.blossom.core.scheduler.IndexationJob;
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
