package fr.mgargadennec.blossom.autoconfigure.core;

import fr.mgargadennec.blossom.core.scheduler.AutowiringSpringBeanJobFactory;
import fr.mgargadennec.blossom.core.scheduler.SampleJob;
import fr.mgargadennec.blossom.core.scheduler.job.ScheduledJobServiceImpl;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.scheduling.quartz.CronTriggerFactoryBean;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.List;
import java.util.Properties;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@PropertySource("classpath:/scheduler.properties")
public class SchedulerAutoConfiguration {

  @Bean
  @Qualifier("sampleJobDetail")
  public JobDetailFactoryBean sampleJobDetail() {
    JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
    factoryBean.setJobClass(SampleJob.class);
    factoryBean.setGroup("sample");
    factoryBean.setName("Sample Job");
    factoryBean.setDescription("Sample job for demonstration purpose");
    factoryBean.setDurability(false);
    return factoryBean;
  }

  @Bean
  public SimpleTriggerFactoryBean sampleJobTrigger(@Qualifier("sampleJobDetail") JobDetail sampleJobDetail) {
    return createFireOnceTrigger(sampleJobDetail);
  }

  @Bean
  public DataSourceInitializer schedulerDatasourceInitializer(DataSource ds, @Value("classpath:quartz_tables.sql") Resource quartzTables) {
    DataSourceInitializer initializer = new DataSourceInitializer();
    initializer.setDataSource(ds);
    initializer.setDatabasePopulator(schedulerDatasourcePopulator(quartzTables));
    return initializer;
  }

  @Bean
  public DatabasePopulator schedulerDatasourcePopulator(Resource quartzTables) {
    final ResourceDatabasePopulator populator = new ResourceDatabasePopulator();
    populator.addScript(quartzTables);
    return populator;
  }

  @Bean
  public JobFactory jobFactory(ApplicationContext applicationContext) {
    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory, List<Trigger> triggers) throws IOException {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setOverwriteExistingJobs(true);
    factory.setAutoStartup(true);
    factory.setDataSource(dataSource);
    factory.setJobFactory(jobFactory);
    factory.setQuartzProperties(quartzProperties());

    // Here we will set all the trigger beans we have defined.
    if (triggers != null && !triggers.isEmpty()) {
      factory.setTriggers(triggers.toArray(new Trigger[triggers.size()]));
    }

    return factory;
  }

  @Bean
  public ScheduledJobServiceImpl jobService(Scheduler scheduler) {
    return new ScheduledJobServiceImpl(scheduler);
  }

  @Bean
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }

  public static SimpleTriggerFactoryBean createFireOnceTrigger(JobDetail jobDetail) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setStartDelay(0L);
    factoryBean.setRepeatCount(0);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
    return factoryBean;
  }

  public static SimpleTriggerFactoryBean createSimpleTrigger(JobDetail jobDetail, long pollFrequencyMs) {
    SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setStartDelay(0L);
    factoryBean.setRepeatInterval(pollFrequencyMs);
    factoryBean.setRepeatCount(SimpleTrigger.REPEAT_INDEFINITELY);
    // in case of misfire, ignore all missed triggers and continue :
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_RESCHEDULE_NEXT_WITH_REMAINING_COUNT);
    return factoryBean;
  }

  // Use this method for creating cron triggers instead of simple triggers:
  public static CronTriggerFactoryBean createCronTrigger(JobDetail jobDetail, String cronExpression) {
    CronTriggerFactoryBean factoryBean = new CronTriggerFactoryBean();
    factoryBean.setJobDetail(jobDetail);
    factoryBean.setCronExpression(cronExpression);
    factoryBean.setMisfireInstruction(SimpleTrigger.MISFIRE_INSTRUCTION_FIRE_NOW);
    return factoryBean;
  }

}
