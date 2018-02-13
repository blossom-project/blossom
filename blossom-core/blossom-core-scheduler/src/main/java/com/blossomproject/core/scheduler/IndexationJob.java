package com.blossomproject.core.scheduler;

import com.blossomproject.core.common.search.IndexationEngine;
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
