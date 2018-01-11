package fr.blossom.core.scheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Trigger;
import org.quartz.impl.JobDetailImpl;
import org.quartz.impl.triggers.SimpleTriggerImpl;
import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

@RunWith(MockitoJUnitRunner.class)
public class AutowiringSpringBeanJobFactoryTest {

  private AutowiringSpringBeanJobFactory factory;

  @Before
  public void setUp() throws Exception {
    this.factory = spy(new AutowiringSpringBeanJobFactory());
  }

  @Test
  public void should_receive_application_context() throws Exception {
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.refresh();
    this.factory.setApplicationContext(context);
  }

  @Test
  public void should_autowired_bean() throws Exception {
    String value = "This is a test string";
    AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
    context.getBeanFactory().registerSingleton("testBean", value);
    context.refresh();

    TriggerFiredBundle bundle = mock(TriggerFiredBundle.class);
    when(bundle.getJobDetail()).thenAnswer(a -> {
      JobDetailImpl jobDetail = new JobDetailImpl();
      jobDetail.setJobClass(TestJob.class);
      return jobDetail;
    });

    when(bundle.getTrigger()).thenAnswer(a -> {
      Trigger trigger = new SimpleTriggerImpl();
      return trigger;
    });

    this.factory.setApplicationContext(context);
    TestJob testJob = (TestJob) this.factory.createJobInstance(bundle);

    assertNotNull(testJob);
    assertNotNull(testJob.getTestBean());
    assertEquals(value, testJob.getTestBean());
  }

  public static class TestJob implements Job {

    @Autowired
    @Qualifier("testBean")
    private String testBean;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {

    }

    public String getTestBean() {
      return testBean;
    }
  }
}
