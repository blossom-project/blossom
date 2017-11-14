package fr.blossom.core.common.actuator;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

import java.time.Instant;

/**
 * Created by Maël Gargadennnec on 17/05/2017.
 */
public class TraceStatisticsEndpoint extends AbstractEndpoint<SearchResponse> {
  private final ElasticsearchTraceRepository traceRepository;

  public TraceStatisticsEndpoint(ElasticsearchTraceRepository traceRepository) {
    super("trace_stats");
    this.traceRepository = traceRepository;
  }


  @Override
  public SearchResponse invoke() {
    return this.invoke(null, null, null);
  }

  public SearchResponse invoke(Instant from, Instant to, String precision) {
    return traceRepository.stats(from, to, precision);
  }
}
