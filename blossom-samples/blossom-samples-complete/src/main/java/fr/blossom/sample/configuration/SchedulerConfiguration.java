package fr.blossom.sample.configuration;

import org.quartz.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Created by Maël Gargadennnec on 24/05/2017.
 */
@Configuration
public class SchedulerConfiguration {

  @Bean
  @Qualifier("sampleJobDetail")
  public JobDetailFactoryBean sampleJobDetail() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(SampleJob.class);
    factoryBean.setGroup("Sample");
    factoryBean.setName("Sample Job");
    factoryBean.setDescription("Sample job for demonstration purpose");
    factoryBean.setDurability(true);
    return factoryBean;
  }

  @Bean
  public CronTriggerFactoryBean sampleJobSimpleTrigger(@Qualifier("sampleJobDetail") JobDetail sampleJobDetail) {
    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    factoryBean.setName("Simple trigger");
    factoryBean.setDescription("This is a simple trigger for demonstration purpose");
    factoryBean.setJobDetail(sampleJobDetail);
    factoryBean.setStartDelay(10L);
    factoryBean.setCronExpression("0/30 * * * * ?");
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_IGNORE_MISFIRE_POLICY);
    return factoryBean;
  }

  @Bean
  public SimpleTriggerFactoryBean sampleJobCronTrigger(@Qualifier("sampleJobDetail") JobDetail sampleJobDetail) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setName("Cron trigger");
    factoryBean.setDescription("This is a cron trigger for demonstration purpose");
    factoryBean.setJobDetail(sampleJobDetail);
    factoryBean.setStartDelay((long) 30 * 1000);
    factoryBean.setRepeatInterval(1 * 60 * 60 * 1000);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }


  /**
   * Created by Maël Gargadennnec on 09/05/2017.
   */
  @DisallowConcurrentExecution
  @PersistJobDataAfterExecution
  public static class SampleJob implements Job {
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

}
