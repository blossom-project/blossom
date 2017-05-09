package fr.mgargadennec.blossom.core.scheduler;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
public class SampleJob implements Job {
  private final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOGGER.info("Sample job execution !");
  }
}
