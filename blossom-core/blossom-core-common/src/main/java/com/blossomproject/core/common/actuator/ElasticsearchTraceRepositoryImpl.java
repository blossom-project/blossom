package com.blossomproject.core.common.actuator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.http.HttpTrace;
import org.springframework.boot.actuate.trace.http.InMemoryHttpTraceRepository;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.AntPathMatcher;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * An implementation of {@link ElasticsearchTraceRepository}
 *
 * @author Maël Gargadennnec
 */
public class ElasticsearchTraceRepositoryImpl extends InMemoryHttpTraceRepository implements
  ElasticsearchTraceRepository {

  private final static Logger logger = LoggerFactory.getLogger(ElasticsearchTraceRepository.class);
  private final static String TIMESTAMP_FIELD = "timestamp";
  private final Client client;
  private final BulkProcessor bulkProcessor;
  private final String index;
  private final List<Pattern> ignoredPatterns;
  private final Set<String> requestHeaderFiltered;
  private final Set<String> responseHeadersFiltered;
  private final AntPathMatcher matcher;
  private final String settings;
  private final ObjectWriter objectWriter;
  private final ObjectMapper objectMapper;

  /**
   * Create a new {@link ElasticsearchTraceRepositoryImpl} using a given Elasticsearch {@link
   * Client}, writing and reading from a given index. This implementation only records traces not
   * excluded in the ignoredUris list of patterns. If the index doesn't exists on the Elasticsearch
   * cluster, a new one will be created with the given settings.
   *
   * @param client                  the elasticsearch client
   * @param bulkProcessor           elasticsearch bulkprocessor
   * @param index                   the index name (serves as alias)
   * @param ignoredUris             a list of regexp patterns to ignore request uris
   * @param requestHeaderFiltered   List of AntMatcher patterns to ignore in request headers
   * @param responseHeadersFiltered List of AntMatcher patterns to ignore in response headers
   * @param settings                the Elasticsearch index setting as json serialized string
   * @param objectMapper            a jackson ObjectMapper to serialize the HttpTrace objects
   */
  public ElasticsearchTraceRepositoryImpl(Client client, BulkProcessor bulkProcessor, String index, List<String> ignoredUris,
                                          Set<String> requestHeaderFiltered, Set<String> responseHeadersFiltered,
                                          String settings, ObjectMapper objectMapper) {
    Preconditions.checkArgument(client != null);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(index));
    Preconditions.checkArgument(ignoredUris != null);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(settings));
    Preconditions.checkArgument(objectMapper != null);
    Preconditions.checkArgument(bulkProcessor != null);
    Preconditions.checkArgument(requestHeaderFiltered != null);
    Preconditions.checkArgument(responseHeadersFiltered != null);

    this.client = client;
    this.index = index;
    this.ignoredPatterns = ignoredUris.stream().map(Pattern::compile)
      .collect(Collectors.toList());
    this.settings = settings;
    this.objectMapper = objectMapper;
    this.objectWriter = objectMapper.writer(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
      SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS,
      SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
    this.bulkProcessor = bulkProcessor;
    this.requestHeaderFiltered = requestHeaderFiltered;
    this.responseHeadersFiltered = responseHeadersFiltered;
    this.matcher = new AntPathMatcher(".");
  }

  /**
   * Initialize the index in the Elasticsearch cluster with the current settings.
   */
  @PostConstruct
  public void initializeIndex() {
    if (!this.client.admin().indices().prepareExists(index).get().isExists()) {
      this.client.admin().indices().prepareCreate(index).setSource(settings).get();
    }
  }

  /**
   * Check if a header should be indexed in this trace repository, and keeps the result in cache
   *
   * @param exludedAntPatterns AntPattern Set of headers that should not be indexed
   * @param headerName         Header name to check against exludedAntPatterns
   * @return true if the header should be removed from indexation
   */
  @Cacheable
  public boolean cachedFilterHeaderOut(Set<String> exludedAntPatterns, String headerName) {
    return exludedAntPatterns.stream().anyMatch(pattern -> matcher.match(pattern.toLowerCase(), headerName.toLowerCase()));
  }

  /**
   * Adds a new {@code HttpTrace} to the repository
   *
   * @param httpTrace the HttpTrace
   */
  @Override
  public void add(HttpTrace httpTrace) {
    String path = httpTrace.getRequest().getUri().getPath();
    if (Strings.isNullOrEmpty(path)) {
      return;
    }

    boolean ignore = ignoredPatterns.stream().anyMatch(pattern -> pattern.matcher(path).matches());
    if (!ignore) {
      super.add(httpTrace);
      try {
        indexTrace(httpTrace);
      } catch (JsonProcessingException e) {
        logger.error("Cannot index trace", e);
      }
    }
  }

  void indexTrace(HttpTrace httpTrace) throws JsonProcessingException {
    ObjectNode document = objectMapper.valueToTree(httpTrace);
    document.put(TIMESTAMP_FIELD, httpTrace.getTimestamp().toEpochMilli());

    if (document.get("request") != null && document.get("request").get("headers") != null) {
      for (Iterator<String> i = document.get("request").get("headers").fieldNames(); i.hasNext(); ) {
        String header = i.next();
        if (cachedFilterHeaderOut(requestHeaderFiltered, header)) {
          i.remove();
        }
      }
    }

    if (document.get("response") != null && document.get("response").get("headers") != null) {
      for (Iterator<String> i = document.get("response").get("headers").fieldNames(); i.hasNext(); ) {
        String header = i.next();
        if (cachedFilterHeaderOut(responseHeadersFiltered, header)) {
          i.remove();
        }
      }
    }

    IndexRequestBuilder request = this.client.prepareIndex(index, index)
      .setSource(objectWriter.writeValueAsString(document));

    bulkProcessor.add(request.request());
  }

  @Override
  public SearchResponse stats(Instant from, Instant to, String precision) {
    if (from == null) {
      from = LocalDate.now().minusDays(7L).atStartOfDay().toInstant(ZoneOffset.UTC);
    }
    if (to == null) {
      to = LocalDate.now().plusDays(1L).atStartOfDay().toInstant(ZoneOffset.UTC);
    }
    if (precision == null) {
      precision = "2h";
    }

    RangeQueryBuilder dateFilter = QueryBuilders.rangeQuery("timestamp").includeLower(true)
      .includeUpper(true);
    dateFilter.from(from.toEpochMilli());
    dateFilter.to(to.toEpochMilli());

    SearchRequestBuilder request = this.client.prepareSearch(this.index)
      .setQuery(QueryBuilders.boolQuery().filter(dateFilter))
      .setSize(0)
      .addAggregation(AggregationBuilders.terms("methods").field("method"))
      .addAggregation(
        AggregationBuilders.histogram("response_time_histogram").field("timeTaken").interval(100))
      .addAggregation(AggregationBuilders.extendedStats("response_time_stats").field("timeTaken"))
      .addAggregation(AggregationBuilders.terms("response_status_stats").field("response.status"))
      .addAggregation(AggregationBuilders.terms("response_content_type_stats")
        .field("response.headers.Content-Type"))
      .addAggregation(AggregationBuilders.terms("top_uris").field("request.uri")
        .order(Terms.Order.aggregation("_count", false)).size(10))
      .addAggregation(AggregationBuilders.terms("flop_uris").field("request.uri")
        .order(Terms.Order.aggregation("_count", true)).size(10))
      .addAggregation(AggregationBuilders.dateHistogram("request_histogram").field("timestamp")
        .interval(new DateHistogramInterval(precision))
        .subAggregation(AggregationBuilders.terms("methods").field("request.method")));
    SearchResponse response = request.execute().actionGet();

    return response;
  }

}
