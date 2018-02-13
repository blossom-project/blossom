package com.blossomproject.core.common.actuator;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
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
import org.springframework.boot.actuate.web.trace.HttpTrace;
import org.springframework.boot.actuate.web.trace.InMemoryHttpTraceRepository;

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
  private final String index;
  private final List<Pattern> ignoredPatterns;
  private final String settings;
  private final ObjectWriter objectWriter;
  private final ObjectMapper objectMapper;

  /**
   * Create a new {@link ElasticsearchTraceRepositoryImpl} using a given Elasticsearch {@link
   * Client}, writing and reading from a given index. This implementation only records traces not
   * excluded in the ignoredUris list of patterns. If the index doesn't exists on the Elasticsearch
   * cluster, a new one will be created with the given settings.
   *
   * @param client the elasticsearch client
   * @param index the index name (serves as alias)
   * @param ignoredUris a list of regexp patterns to ignore request uris
   * @param settings the Elasticsearch index setting as json serialized string
   * @param objectMapper a jackson ObjectMapper to serialize the HttpTrace objects
   */
  public ElasticsearchTraceRepositoryImpl(Client client, String index, List<String> ignoredUris,
    String settings, ObjectMapper objectMapper) {
    Preconditions.checkArgument(client != null);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(index));
    Preconditions.checkArgument(ignoredUris != null);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(settings));
    Preconditions.checkArgument(objectMapper != null);

    this.client = client;
    this.index = index;
    this.ignoredPatterns = ignoredUris.stream().map(pattern -> Pattern.compile(pattern))
      .collect(Collectors.toList());
    this.settings = settings;
    this.objectMapper=objectMapper;
    this.objectWriter = objectMapper.writer(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS,
      SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS,
      SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);
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
   * Adds a new {@code HttpTrace} to the repository
   *
   * @param httpTrace the HttpTrace
   */
  @Override
  public void add(HttpTrace httpTrace) {
    String path = httpTrace.getRequest().getUri().getPath().toString();
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
    this.client.prepareIndex(index, index)
      .setSource(objectWriter.writeValueAsString(document))
      .execute(new ActionListener<IndexResponse>() {
        @Override
        public void onResponse(IndexResponse indexResponse) {
          logger.trace("Indexed trace into elasticsearch {}", httpTrace);
        }

        @Override
        public void onFailure(Throwable t) {
          logger.error("Indexed trace into elasticsearch {} {}", httpTrace, t);
        }
      });
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
      .addAggregation(AggregationBuilders.terms("response_content_type_stats").field("response.headers.Content-Type"))
      .addAggregation(AggregationBuilders.terms("top_uris").field("request.uri").order(Terms.Order.aggregation("_count", false)).size(10))
      .addAggregation(AggregationBuilders.terms("flop_uris").field("request.uri").order(Terms.Order.aggregation("_count", true)).size(10))
      .addAggregation(AggregationBuilders.dateHistogram("request_histogram").field("timestamp").interval(new DateHistogramInterval(precision))
        .subAggregation(AggregationBuilders.terms("methods").field("request.method")));
    SearchResponse response = request.execute().actionGet();

    return response;
  }

}
