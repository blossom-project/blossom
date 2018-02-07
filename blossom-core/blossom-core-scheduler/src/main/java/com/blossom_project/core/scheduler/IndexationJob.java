package com.blossom_project.core.scheduler;

import com.blossom_project.core.common.search.IndexationEngine;
import org.quartz.*;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
@PersistJobDataAfterExecution
@DisallowConcurrentExecution
public abstract class IndexationJob implements Job {

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    getIndexationEngine().indexFull();
  }

  protected abstract IndexationEngine getIndexationEngine();
}
