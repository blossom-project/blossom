package fr.blossom.autoconfigure.core;

import fr.blossom.core.scheduler.AutowiringSpringBeanJobFactory;
import fr.blossom.core.scheduler.job.ScheduledJobServiceImpl;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

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
  public JobFactory jobFactory(ApplicationContext applicationContext) {
    AutowiringSpringBeanJobFactory jobFactory = new AutowiringSpringBeanJobFactory();
    jobFactory.setApplicationContext(applicationContext);
    return jobFactory;
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory, List<Trigger> triggers, @Value("${blossom.scheduler.name}") String schedulerName) throws IOException {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
    factory.setSchedulerName(schedulerName);
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
  public Properties quartzProperties() throws IOException {
    PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
    propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.properties"));
    propertiesFactoryBean.afterPropertiesSet();
    return propertiesFactoryBean.getObject();
  }

  @Bean
  public ScheduledJobServiceImpl scheduledJobService(Scheduler scheduler) {
    return new ScheduledJobServiceImpl(scheduler);
  }

}
