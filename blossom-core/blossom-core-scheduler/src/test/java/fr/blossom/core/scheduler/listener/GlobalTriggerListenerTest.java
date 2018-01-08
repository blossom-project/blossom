package fr.blossom.core.scheduler.listener;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.mockito.Mockito.when;

import fr.blossom.core.scheduler.history.TriggerHistory;
import fr.blossom.core.scheduler.history.TriggerHistoryDao;
import java.util.Date;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.quartz.JobExecutionContext;
import org.quartz.JobKey;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.TriggerKey;

@RunWith(MockitoJUnitRunner.class)
public class GlobalTriggerListenerTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private GlobalTriggerListener listener;
  private TriggerHistoryDao triggerHistoryDao;

  @Before
  public void setUp() throws Exception {
    this.triggerHistoryDao = mock(TriggerHistoryDao.class);
    this.listener = spy(new GlobalTriggerListener(this.triggerHistoryDao));
  }

  @Test
  public void should_succeed_instanciate() throws Exception {
    new GlobalTriggerListener(this.triggerHistoryDao);
  }

  @Test
  public void should_fail_instanciate_with_null_dao() throws Exception {
    thrown.expect(NullPointerException.class);
    new GlobalTriggerListener(null);
  }

  @Test
  public void should_return_name(){
    assertEquals(this.listener.getName(), GlobalTriggerListener.NAME);
  }

  @Test
  public void should_do_nothing_on_veto_and_return_false() throws Exception{
    boolean veto = this.listener.vetoJobExecution(null,null);
    assertFalse(veto);
    verifyZeroInteractions(this.triggerHistoryDao);
  }

  @Test
  public void should_do_nothing_on_misfired() throws Exception{
    this.listener.triggerMisfired(null);
    verifyZeroInteractions(this.triggerHistoryDao);
  }


  @Test
  public void should_record_history_on_trigger_fired() throws Exception{
    Trigger trigger = mock(Trigger.class);
    when(trigger.getKey()).thenReturn(new TriggerKey("trigger","group"));
    when(trigger.getJobKey()).thenReturn(new JobKey("job","group"));

    JobExecutionContext context = mock(JobExecutionContext.class);
    when(context.getFireInstanceId()).thenReturn("fireInstanceId");
    when(context.getFireTime()).thenReturn(new Date(System.currentTimeMillis()));

    this.listener.triggerFired(trigger, context);

    verify(this.triggerHistoryDao, times(1)).create(any(TriggerHistory.class));
  }

  @Test
  public void should_update_history_on_trigger_completed() throws Exception{
    Trigger trigger = mock(Trigger.class);
    when(trigger.getKey()).thenReturn(new TriggerKey("trigger","group"));
    when(trigger.getJobKey()).thenReturn(new JobKey("job","group"));

    JobExecutionContext context = mock(JobExecutionContext.class);
    when(context.getFireInstanceId()).thenReturn("fireInstanceId");
    when(context.getFireTime()).thenReturn(new Date(System.currentTimeMillis()));

    CompletedExecutionInstruction instruction = CompletedExecutionInstruction.SET_TRIGGER_COMPLETE;

    this.listener.triggerComplete(trigger, context, instruction);

    verify(this.triggerHistoryDao, times(1)).updateEndDate(eq(context.getFireInstanceId()));
  }

}
