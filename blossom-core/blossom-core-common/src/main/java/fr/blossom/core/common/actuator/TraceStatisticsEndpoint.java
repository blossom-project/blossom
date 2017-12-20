package fr.blossom.core.common.actuator;

import java.time.Instant;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;
import org.springframework.boot.actuate.endpoint.Endpoint;

/**
 * {@link Endpoint} to expose {@link org.springframework.boot.actuate.trace.Trace} statistics.
 *
 * @author Maël Gargadennec
 */
public class TraceStatisticsEndpoint extends AbstractEndpoint<SearchResponse> {

  /**
   * Used {@link ElasticsearchTraceRepository} to store {@link org.springframework.boot.actuate.trace.Trace}
   * and compute stats
   */
  private final ElasticsearchTraceRepository traceRepository;

  /**
   * Create a new {@link TraceStatisticsEndpoint} from a given {@link ElasticsearchTraceRepository}
   * Registers it with a fixed endpoint identifier.
   *
   * @param traceRepository the {@link ElasticsearchTraceRepository} to use
   */
  public TraceStatisticsEndpoint(ElasticsearchTraceRepository traceRepository) {
    super("trace_stats");
    this.traceRepository = traceRepository;
  }

  /**
   * Invoke the endpoint with default values
   *
   * @return the {@link SearchResponse} containing the stats
   */
  @Override
  public SearchResponse invoke() {
    return this.invoke(null, null, null);
  }

  /**
   * Invoke the endpoint with custom values
   *
   * @return the {@link SearchResponse} containing the filtered stats
   */
  public SearchResponse invoke(Instant from, Instant to, String precision) {
    return traceRepository.stats(from, to, precision);
  }
}
