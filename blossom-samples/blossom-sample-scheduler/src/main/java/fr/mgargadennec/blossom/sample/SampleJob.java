package fr.mgargadennec.blossom.sample;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Maël Gargadennnec on 09/05/2017.
 */
@DisallowConcurrentExecution
@PersistJobDataAfterExecution
public class SampleJob implements Job {
  private final Logger LOGGER = LoggerFactory.getLogger(SampleJob.class);

  @Override
  public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
    LOGGER.info("Sample job execution !");
    try {
      Thread.sleep(15000L);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
