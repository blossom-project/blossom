package fr.blossom.core.common.search;

import com.google.common.collect.Lists;
import java.util.List;
import org.elasticsearch.search.aggregations.Aggregation;
import org.springframework.data.domain.Page;

/**
 * Created by Maël Gargadennnec on 23/05/2017.
 */
public class SearchResult<DTO> {
  private final Page<DTO> page;
  private final List<Aggregation> aggregations;

  public SearchResult(Page<DTO> page) {
    this(page, Lists.newArrayList());
  }

  public SearchResult(Page<DTO> page, List<Aggregation> aggregations) {
    this.page = page;
    this.aggregations = aggregations;
  }

  public Page<DTO> getPage() {
    return page;
  }

  public List<Aggregation> getAggregations() {
    return aggregations;
  }
}
