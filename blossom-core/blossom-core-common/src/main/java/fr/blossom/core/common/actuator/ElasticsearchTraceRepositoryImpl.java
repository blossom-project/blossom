package fr.blossom.core.common.actuator;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
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
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.Trace;

/**
 * An implementation of {@link ElasticsearchTraceRepository}
 *
 * @author Maël Gargadennnec
 */
public class ElasticsearchTraceRepositoryImpl implements ElasticsearchTraceRepository {

  private final static Logger logger = LoggerFactory.getLogger(ElasticsearchTraceRepository.class);
  private final static String TIMESTAMP_FIELD = "timestamp";
  private final Client client;
  private final String index;
  private final List<Pattern> ignoredPatterns;
  private final String settings;

  /**
   * Create a new {@link ElasticsearchTraceRepositoryImpl} using a given Elasticsearch {@link
   * Client}, writing and reading from a given index. This implementation only records traces not
   * excluded in the ignoredUris list of patterns. If the index doesn't exists on the Elasticsearch
   * cluster, a new one will be created with the given settings.
   */
  public ElasticsearchTraceRepositoryImpl(Client client, String index, List<String> ignoredUris,
    String settings) {
    Preconditions.checkArgument(client != null);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(index));
    Preconditions.checkArgument(ignoredUris != null);
    Preconditions.checkArgument(!Strings.isNullOrEmpty(settings));

    this.client = client;
    this.index = index;
    this.ignoredPatterns = ignoredUris.stream().map(pattern -> Pattern.compile(pattern))
      .collect(Collectors.toList());
    this.settings = settings;
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
   * Find the last 100 {@code Trace} stored in the repository
   *
   * @return a list of {@code Trace}
   */
  @Override
  public List<Trace> findAll() {
    SearchResponse response = client.prepareSearch(index)
      .setQuery(QueryBuilders.matchAllQuery())
      .setSize(100)
      .addSort(TIMESTAMP_FIELD, SortOrder.DESC)
      .get();

    return Stream.of(response.getHits().getHits())
      .map(hit -> {
        Map<String, Object> source = hit.getSource();
        return new Trace(new Date((Long) source.get(TIMESTAMP_FIELD)), source);
      }).collect(Collectors.toList());
  }

  /**
   * Adds a new {@code Trace} to the repository
   *
   * @param traceInfo the infos of the Trace
   */
  @Override
  public void add(Map<String, Object> traceInfo) {
    String path = (String) traceInfo.get("path");
    if (Strings.isNullOrEmpty(path)) {
      return;
    }

    boolean ignore = ignoredPatterns.stream().anyMatch(pattern -> pattern.matcher(path).matches());
    if (!ignore) {
      indexTrace(traceInfo);
    }
  }

  void indexTrace(Map<String, Object> traceInfo) {
    traceInfo.put(TIMESTAMP_FIELD, Instant.now().toEpochMilli());

    this.client.prepareIndex(index, index).setSource(traceInfo)
      .execute(new ActionListener<IndexResponse>() {
        @Override
        public void onResponse(IndexResponse indexResponse) {
          logger.trace("Indexed trace into elasticsearch {}", traceInfo);
        }

        @Override
        public void onFailure(Throwable t) {
          logger.error("Indexed trace into elasticsearch {} {}", traceInfo, t);
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
      .addAggregation(
        AggregationBuilders.terms("response_status_stats").field("headers.response.status"))
      .addAggregation(AggregationBuilders.terms("response_content_type_stats")
        .field("headers.response.Content-Type"))
      .addAggregation(AggregationBuilders.terms("top_uris").field("path")
        .order(Terms.Order.aggregation("_count", false)).size(10))
      .addAggregation(AggregationBuilders.terms("flop_uris").field("path")
        .order(Terms.Order.aggregation("_count", true)).size(10))
      .addAggregation(AggregationBuilders.dateHistogram("request_histogram").field("timestamp")
        .interval(new DateHistogramInterval(precision))
        .subAggregation(AggregationBuilders.terms("methods").field("method")));
    SearchResponse response = request.execute().actionGet();

    return response;
  }

}
