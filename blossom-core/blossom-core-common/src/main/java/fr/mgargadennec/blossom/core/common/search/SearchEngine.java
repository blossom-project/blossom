package fr.mgargadennec.blossom.core.common.search;


import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.plugin.core.Plugin;

public interface SearchEngine extends Plugin<Class<? extends AbstractDTO>> {

  SearchResult<?> search(String q, Pageable pageable);

  SearchResult<?> search(String q, Pageable pageable, Iterable<QueryBuilder> filters);

  SearchResult<?> search(String q, Pageable pageable, Iterable<QueryBuilder> filters, Iterable<AggregationBuilder> aggregations);

}
