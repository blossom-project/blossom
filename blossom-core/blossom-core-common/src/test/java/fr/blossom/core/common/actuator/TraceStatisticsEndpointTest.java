package fr.blossom.core.common.actuator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;


@RunWith(MockitoJUnitRunner.class)
public class TraceStatisticsEndpointTest {

  private ElasticsearchTraceRepository traceRepository;
  private TraceStatisticsEndpoint endpoint;

  @Before
  public void setUp() {
    this.traceRepository = mock(ElasticsearchTraceRepository.class);
    this.endpoint = spy(new TraceStatisticsEndpoint(traceRepository));
  }

  @Test
  public void should_have_the_right_name() {
    assertEquals("trace_stats", this.endpoint.getId());
  }
  @Test
  public void should_be_enabled() {
    assertTrue(this.endpoint.isEnabled());
  }
  @Test
  public void should_be_sensitive() {
    assertTrue(this.endpoint.isSensitive());
  }

  @Test
  public void should_invoke() {
    this.endpoint.invoke();
    verify(this.endpoint, times(1)).invoke(isNull(Instant.class),isNull(Instant.class),isNull(String.class));
  }

  @Test
  public void should_invoke_with_empty_parameters() {
    this.endpoint.invoke(null, null, null);
    verify(this.traceRepository, times(1)).stats(isNull(Instant.class),isNull(Instant.class),isNull(String.class));
  }

  @Test
  public void should_invoke_with_parameters() {
    Instant from  = Instant.now().minus(5, ChronoUnit.HOURS);
    Instant to = Instant.now();
    String precision = "2h";
    this.endpoint.invoke(from, to,precision);
    verify(this.traceRepository, times(1)).stats(eq(from),eq(to),eq(precision));
  }
}
