package com.blossomproject.core.common.actuator;

import java.time.Instant;
import org.elasticsearch.action.search.SearchResponse;
import org.springframework.boot.actuate.web.trace.HttpTraceRepository;

/**
 * An extended {@link HttpTraceRepository} that records {@link org.springframework.boot.actuate.web.trace.HttpTrace} into an Elasticsearch index.
 *
 * @author Maël Gargadennec
 */
public interface ElasticsearchTraceRepository extends HttpTraceRepository {

  /**
   * Compute some statistics from the Elasticsearch index, filtered on dates and with a period precision.
   * Statistics are in the form of aggregations.
   *
   * @param from the lower boundary of the traces taken into account (included). Can be null.
   * @param to the upper boundary of the traces taken into account (included). Can be null.
   * @param precision the precision of intervals (must be understood as an Elasticsearch time unit). Can be null.
   * @return the {@link SearchResponse}
   *
   * @see <a href="https://www.elastic.co/guide/en/elasticsearch/reference/current/common-options.html#time-units">Elastiscearch TimeUnit</a>
   */
  SearchResponse stats(Instant from, Instant to, String precision);

}
