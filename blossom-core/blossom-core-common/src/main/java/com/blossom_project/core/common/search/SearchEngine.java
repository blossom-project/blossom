package com.blossom_project.core.common.search;


import com.blossom_project.core.common.PluginConstants;
import com.blossom_project.core.common.dto.AbstractDTO;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Pageable;
import org.springframework.plugin.core.Plugin;

@Qualifier(value = PluginConstants.PLUGIN_SEARCH_ENGINE)
public interface SearchEngine extends Plugin<Class<? extends AbstractDTO>> {

  String getName();

  boolean includeInOmnisearch();

  SearchRequestBuilder prepareSearch(String q, Pageable pageable);

  SearchRequestBuilder prepareSearch(String q, Pageable pageable, Iterable<QueryBuilder> filters);

  SearchRequestBuilder prepareSearch(String q, Pageable pageable, Iterable<QueryBuilder> filters, Iterable<AggregationBuilder> aggregations);

  SearchResult<?> parseResults(SearchResponse response, Pageable pageable);

  SearchResult<SummaryDTO> parseSummaryResults(SearchResponse response, Pageable pageable);

  SearchResult<?> search(String q, Pageable pageable);

  SearchResult<?> search(String q, Pageable pageable, Iterable<QueryBuilder> filters);

  SearchResult<?> search(String q, Pageable pageable, Iterable<QueryBuilder> filters, Iterable<AggregationBuilder> aggregations);

}
