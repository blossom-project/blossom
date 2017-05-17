package fr.mgargadennec.blossom.core.common.actuator;

import org.elasticsearch.action.search.SearchResponse;
import org.springframework.boot.actuate.trace.TraceRepository;

import java.time.Instant;

/**
 * Created by maelg on 16/05/2017.
 */
public interface ElasticsearchTraceRepository extends TraceRepository {

  SearchResponse stats(Instant from, Instant to);

}
