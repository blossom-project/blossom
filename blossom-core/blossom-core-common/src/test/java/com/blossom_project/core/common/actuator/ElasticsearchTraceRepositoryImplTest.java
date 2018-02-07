package com.blossom_project.core.common.actuator;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;
import java.util.List;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.actuate.web.trace.HttpTrace;
import org.springframework.boot.actuate.web.trace.HttpTraceCreator;

;

@RunWith(MockitoJUnitRunner.class)
public class ElasticsearchTraceRepositoryImplTest {

  private Client client;
  private List<String> ignoredPatterns;
  private String alias = "test";
  private ElasticsearchTraceRepositoryImpl repository;
  private ObjectMapper objectMapper;

  @Before
  public void setUp() {
    this.client = mock(Client.class);
    this.ignoredPatterns = spy(Lists.newArrayList("/ignored.*"));
    this.alias = "test";
    this.objectMapper = new ObjectMapper();
    this.repository = spy(
      new ElasticsearchTraceRepositoryImpl(client, "test", ignoredPatterns, "{}", objectMapper));

    doNothing().when(this.repository).initializeIndex();
  }

  @Test
  public void should_initialize_correctly() {
    this.repository.initializeIndex();
  }

  @Test
  public void should_not_add_ignored_trace_info() throws JsonProcessingException {
    this.repository.add(HttpTraceCreator.createHttpTraceMock("/ignored/test"));
    verify(this.repository, times(0)).indexTrace(any(HttpTrace.class));
  }


  @Test
  public void should_add_trace_info() throws JsonProcessingException {
    HttpTrace traceInfo = HttpTraceCreator.createHttpTraceMock("/test");
    String serialized = "{\"test\":\"test\"}";

    IndexRequestBuilder mockedRequest = mock(IndexRequestBuilder.class);
    when(mockedRequest.setSource(anyString())).thenReturn(mockedRequest);
    doNothing().when(mockedRequest).execute(any(ActionListener.class));

    when(this.client.prepareIndex(eq(alias), eq(alias))).thenReturn(mockedRequest);

    this.repository.add(traceInfo);
    verify(this.repository, times(1)).indexTrace(eq(traceInfo));
    verify(mockedRequest, times(1)).setSource(anyString());
    verify(mockedRequest, times(1)).execute(any(ActionListener.class));
  }

  @Test
  public void should_find_all_when_empty() {
    Long currentTimestamp = System.currentTimeMillis();

    List<HttpTrace> traces = this.repository.findAll();

    assertTrue(traces.isEmpty());
  }

  @Test
  public void should_find_all() throws JsonProcessingException {
    HttpTrace traceInfo = HttpTraceCreator.createHttpTraceMock("/test");
    String serialized = "{\"test\":\"test\"}";

    IndexRequestBuilder mockedRequest = mock(IndexRequestBuilder.class);
    when(mockedRequest.setSource(anyString())).thenReturn(mockedRequest);
    doNothing().when(mockedRequest).execute(any(ActionListener.class));

    when(this.client.prepareIndex(eq(alias), eq(alias))).thenReturn(mockedRequest);


    this.repository.add(traceInfo);
    List<HttpTrace> traces = this.repository.findAll();
    assertFalse(traces.isEmpty());
    assertEquals(traceInfo, traces.get(0));
  }
}
