package fr.mgargadennec.blossom.core.common.actuator;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.boot.actuate.endpoint.AbstractEndpoint;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

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
    return this.invoke(LocalDate.now().minusDays(7L).atStartOfDay().toInstant(ZoneOffset.UTC), LocalDate.now().plusDays(1).atStartOfDay().toInstant(ZoneOffset.UTC));
  }

  public SearchResponse invoke(Instant from, Instant to) {
    return traceRepository.stats(from, to);
  }
}
