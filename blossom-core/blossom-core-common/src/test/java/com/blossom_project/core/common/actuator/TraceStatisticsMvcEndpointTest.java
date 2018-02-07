package com.blossom_project.core.common.actuator;

//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.Matchers.any;
//import static org.mockito.Matchers.isNull;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.spy;
//import static org.mockito.Mockito.times;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.blossom_project.core.common.actuator.TraceStatisticsMvcEndpoint.Period;
//import java.time.Instant;
//import org.elasticsearch.action.search.SearchResponse;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.junit.MockitoJUnitRunner;;


//@RunWith(MockitoJUnitRunner.class)
//public class TraceStatisticsMvcEndpointTest {
//
//  private TraceStatisticsEndpoint endpoint;
//  private TraceStatisticsMvcEndpoint mvcEndpoint;
//
//  @Before
//  public void setUp() {
//    this.endpoint = mock(TraceStatisticsEndpoint.class);
//    when(this.endpoint.getId()).thenReturn("traces_stats");
//    when(this.endpoint.isEnabled()).thenReturn(true);
//    when(this.endpoint.isSensitive()).thenReturn(true);
//    this.mvcEndpoint = spy(new TraceStatisticsMvcEndpoint(endpoint));
//  }
//
//  @Test
//  public void should_have_correct_name() {
//    assertEquals("traces_stats", this.mvcEndpoint.getName());
//  }
//
//  @Test
//  public void should_be_sensitive() {
//    assertTrue(this.mvcEndpoint.isSensitive());
//  }
//
//  @Test
//  public void should_invoke() {
//    this.mvcEndpoint.invoke();
//    verify(this.endpoint, times(1)) .invoke();
//  }
//
//  @Test
//  public void should_invoke_with_empty_parameters() {
//    SearchResponse mockedResponse = mock(SearchResponse.class);
//    String mockedResponseString = "{test}";
//    when(this.endpoint.invoke(isNull(Instant.class),isNull(Instant.class),isNull(String.class))).thenReturn(mockedResponse);
//    when(mockedResponse.toString()).thenReturn(mockedResponseString);
//    String response = this.mvcEndpoint.invoke(null);
//    verify(this.endpoint, times(1)).invoke(isNull(Instant.class),isNull(Instant.class),isNull(String.class));
//    assertEquals(response, mockedResponseString);
//  }
//
//  @Test
//  public void should_invoke_with_parameters() {
//    SearchResponse mockedResponse = mock(SearchResponse.class);
//    String mockedResponseString = "{test}";
//    when(this.endpoint.invoke(any(Instant.class),any(Instant.class),any(String.class))).thenReturn(mockedResponse);
//    when(mockedResponse.toString()).thenReturn(mockedResponseString);
//
//    String response = this.mvcEndpoint.invoke(Period.PAST_3_DAYS);
//    verify(this.endpoint, times(1)).invoke(any(Instant.class),any(Instant.class),any(String.class));
//    assertEquals(response, mockedResponseString);
//  }
//}
