package fr.blossom.core.common.actuator;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyMap;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.boot.actuate.trace.Trace;

@RunWith(MockitoJUnitRunner.class)
public class ElasticsearchTraceRepositoryImplTest {

  private Client client;
  private List<String> ignoredPatterns;
  private String alias = "test";
  private ElasticsearchTraceRepositoryImpl repository;

  @Before
  public void setUp() {
    this.client = mock(Client.class);
    this.ignoredPatterns = spy(Lists.newArrayList("/ignored.*"));
    this.alias = "test";
    this.repository = spy(
      new ElasticsearchTraceRepositoryImpl(client, "test", ignoredPatterns, "{}"));

    doNothing().when(this.repository).initializeIndex();
  }

  @Test
  public void should_initialize_correctly() {
    this.repository.initializeIndex();
  }

  @Test
  public void should_add_empty_trace_info() {
    this.repository.add(Maps.newHashMap());
    verify(this.repository, times(0)).indexTrace(anyMap());
  }

  @Test
  public void should_add_trace_without_path_info() {
    Map<String, Object> traceInfo = Maps.newHashMap();
    traceInfo.put("test", "test");

    this.repository.add(traceInfo);
    verify(this.repository, times(0)).indexTrace(eq(traceInfo));
  }

  @Test
  public void should_add_trace_info() {
    Map<String, Object> traceInfo = spy(Maps.newHashMap());
    traceInfo.put("path", "test");

    IndexRequestBuilder mockedRequest = mock(IndexRequestBuilder.class);
    when(mockedRequest.setSource(eq(traceInfo))).thenReturn(mockedRequest);
    doNothing().when(mockedRequest).execute(any(ActionListener.class));

    when(this.client.prepareIndex(eq(alias), eq(alias))).thenReturn(mockedRequest);

    this.repository.add(traceInfo);
    verify(traceInfo, times(1)).put(eq("timestamp"), any());
    verify(this.repository, times(1)).indexTrace(eq(traceInfo));
    verify(mockedRequest, times(1)).setSource(eq(traceInfo));
    verify(mockedRequest, times(1)).execute(any(ActionListener.class));
  }

  @Test
  public void should_find_all_when_empty() {
    Long currentTimestamp = System.currentTimeMillis();
    SearchRequestBuilder mockedRequest = mock(SearchRequestBuilder.class);
    SearchResponse mockedResponse = mock(SearchResponse.class);
    SearchHits searchHits = mock(SearchHits.class);

    when(this.client.prepareSearch(eq(alias))).thenReturn(mockedRequest);
    when(mockedRequest.setQuery(any(QueryBuilder.class))).thenReturn(mockedRequest);
    when(mockedRequest.setSize(any(Integer.class))).thenReturn(mockedRequest);
    when(mockedRequest.addSort(anyString(), any(SortOrder.class))).thenReturn(mockedRequest);
    when(mockedRequest.get()).thenReturn(mockedResponse);
    when(mockedResponse.getHits()).thenReturn(searchHits);
    when(searchHits.getHits()).thenReturn(new SearchHit[]{});

    List<Trace> traces = this.repository.findAll();

    assertTrue(traces.isEmpty());
    verify(this.client, times(1)).prepareSearch(eq(alias));
    verify(mockedRequest, times(1)).setQuery(any(QueryBuilder.class));
    verify(mockedRequest, times(1)).setSize(eq(100));
    verify(mockedRequest, times(1)).addSort(eq("timestamp"), eq(SortOrder.DESC));
  }

  @Test
  public void should_find_all() {
    Long currentTimestamp = System.currentTimeMillis();
    SearchRequestBuilder mockedRequest = mock(SearchRequestBuilder.class);
    SearchResponse mockedResponse = mock(SearchResponse.class);
    SearchHits searchHits = mock(SearchHits.class);
    SearchHit hit = mock(SearchHit.class);

    when(this.client.prepareSearch(eq(alias))).thenReturn(mockedRequest);
    when(mockedRequest.setQuery(any(QueryBuilder.class))).thenReturn(mockedRequest);
    when(mockedRequest.setSize(any(Integer.class))).thenReturn(mockedRequest);
    when(mockedRequest.addSort(anyString(), any(SortOrder.class))).thenReturn(mockedRequest);
    when(mockedRequest.get()).thenReturn(mockedResponse);
    when(mockedResponse.getHits()).thenReturn(searchHits);
    when(searchHits.getHits()).thenReturn(new SearchHit[]{hit});
    when(hit.getSource()).thenAnswer(a -> {
      Map<String, Object> source = Maps.newHashMap();
      source.put("timestamp", currentTimestamp);
      source.put("test", "test");
      return source;
    });

    List<Trace> traces = this.repository.findAll();

    assertTrue(!traces.isEmpty());
    assertTrue(traces.get(0).getTimestamp().getTime() == currentTimestamp);
    assertEquals((String) traces.get(0).getInfo().get("test"), "test");
    verify(this.client, times(1)).prepareSearch(eq(alias));
    verify(mockedRequest, times(1)).setQuery(any(QueryBuilder.class));
    verify(mockedRequest, times(1)).setSize(eq(100));
    verify(mockedRequest, times(1)).addSort(eq("timestamp"), eq(SortOrder.DESC));
  }
}
