package fr.mgargadennec.blossom.core.scheduler;

import fr.mgargadennec.blossom.core.common.search.IndexationEngine;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
public abstract class IndexationJob implements Job {

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    getIndexationEngine().indexFull();
  }

  protected abstract IndexationEngine getIndexationEngine();
}
