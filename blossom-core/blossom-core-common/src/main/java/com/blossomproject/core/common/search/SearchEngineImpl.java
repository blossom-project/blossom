package com.blossomproject.core.common.search;

import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.core.common.search.facet.AggregationConverter;
import com.blossomproject.core.common.search.facet.Facet;
import com.blossomproject.core.common.search.facet.FacetConfiguration;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.Optional;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.global.Global;
import org.elasticsearch.search.aggregations.bucket.global.GlobalBuilder;
import org.elasticsearch.search.sort.SortBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.plugin.core.PluginRegistry;

public class SearchEngineImpl<DTO extends AbstractDTO> implements SearchEngine {
  private final static String GLOBAL_AGGREGATION = "global";

  private final Client client;
  private final ObjectMapper objectMapper;
  private final PluginRegistry<AggregationConverter, SearchEngine> aggregationRegistry;
  private final SearchEngineConfiguration<DTO> configuration;

  public SearchEngineImpl(Client client, ObjectMapper objectMapper,
    PluginRegistry<AggregationConverter, SearchEngine> aggregationRegistry,
    SearchEngineConfiguration<DTO> configuration) {
    this.client = client;
    this.objectMapper = objectMapper;
    this.aggregationRegistry = aggregationRegistry;
    this.configuration = configuration;
  }

  @Override
  public String getName() {
    return this.configuration.getName();
  }

  @Override
  public boolean includeInOmnisearch() {
    return this.configuration.includeInOmnisearch();
  }

  public SearchResult<DTO> search(String q, Pageable pageable) {
    return this.search(q, pageable, null);
  }

  @Override
  public SearchResult<DTO> search(String q, Pageable pageable, Iterable<QueryBuilder> filters) {
    return this.search(q, pageable, filters, null);
  }

  @Override
  public SearchResult<DTO> search(String q, Pageable pageable, Iterable<QueryBuilder> filters,
    Iterable<FacetConfiguration> facetConfigurations) {
    SearchRequestBuilder searchRequest = prepareSearch(q, pageable, filters, facetConfigurations);
    SearchResponse searchResponse = searchRequest.get(TimeValue.timeValueSeconds(10));
    return parseResults(searchResponse, pageable, facetConfigurations);
  }

  @Override
  public SearchRequestBuilder prepareSearch(String q, Pageable pageable) {
    return prepareSearch(q, pageable, null);
  }

  @Override
  public SearchRequestBuilder prepareSearch(String q, Pageable pageable,
    Iterable<QueryBuilder> filters) {
    return prepareSearch(q, pageable, filters, null);
  }

  @Override
  public SearchRequestBuilder prepareSearch(String q, Pageable pageable,
    Iterable<QueryBuilder> filters,
    Iterable<FacetConfiguration> facetConfigurations) {

    QueryBuilder initialQuery;
    String[] searchableFields = this.configuration.getFields();

    if (Strings.isNullOrEmpty(q)) {
      initialQuery = QueryBuilders.matchAllQuery();
    } else if (searchableFields == null) {
      initialQuery = QueryBuilders.simpleQueryStringQuery(q);
    } else {
      initialQuery = QueryBuilders.multiMatchQuery(q, searchableFields).type(Type.CROSS_FIELDS)
        .lenient(true);
    }

    BoolQueryBuilder query = QueryBuilders.boolQuery().must(initialQuery);

    if (filters != null) {
      for (QueryBuilder filter : filters) {
        query = query.filter(filter);
      }
    }

    SearchRequestBuilder searchRequest = this.client.prepareSearch(this.configuration.getAlias())
      .setQuery(query)
      .setSize(pageable.getPageSize()).setFrom((int) pageable.getOffset());

    if(facetConfigurations!=null) {
      GlobalBuilder global = AggregationBuilders.global(GLOBAL_AGGREGATION);
      boolean addGlobal = false;

      List<AggregationConverter> aggregationConverters = aggregationRegistry.getPluginsFor(this);
      for(FacetConfiguration facetConfiguration : facetConfigurations){
        if (!aggregationConverters.isEmpty()) {
          Optional<AggregationConverter> converter = aggregationConverters.stream().filter(c -> c.name().equals(facetConfiguration.getName())).findAny();
          if (converter.isPresent()) {
            List<AggregationBuilder> builders = converter.get().encode(facetConfiguration);
            if(facetConfiguration.isGlobal()){
              builders.forEach(global::subAggregation);
              addGlobal=true;
            }else{
              builders.forEach(searchRequest::addAggregation);
            }
          }
        }
      }
      if(addGlobal){
        searchRequest.addAggregation(global);
      }
    }

    Sort sort = pageable.getSort();
    if (sort != null) {
      for (Order order : pageable.getSort()) {
        SortBuilder sortBuilder = SortBuilders.fieldSort("dto." + order.getProperty())
          .order(SortOrder.valueOf(order.getDirection().name()));
        searchRequest.addSort(sortBuilder);
      }
      searchRequest.addSort(SortBuilders.scoreSort());
    }

    return searchRequest;
  }

  @Override
  public SearchResult<DTO> parseResults(SearchResponse searchResponse, Pageable pageable) {
    return this.parseResults(searchResponse, pageable,  null);
  }

  @Override
  public SearchResult<DTO> parseResults(SearchResponse searchResponse, Pageable pageable, Iterable<FacetConfiguration> facetConfigurations) {
    return this.doParseResults(searchResponse, pageable, "dto", this.configuration.getSupportedClass(), facetConfigurations);
  }

  @Override
  public SearchResult<SummaryDTO> parseSummaryResults(SearchResponse searchResponse, Pageable pageable) {
    return this.doParseResults(searchResponse, pageable, "summary", SummaryDTO.class, null);
  }

  private <T> SearchResult<T> doParseResults(SearchResponse searchResponse, Pageable pageable,
    String field, Class<T> targetClass, Iterable<FacetConfiguration> facetConfigurations) {
    List<T> resultList = Lists.newArrayList();
    for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
      try {
        SearchHit hit = searchResponse.getHits().getHits()[i];
        JsonNode document = this.objectMapper.readTree(hit.getSourceAsString());
        T result = this.objectMapper.treeToValue(document.get(field), targetClass);
        resultList.add(result);
      } catch (Exception e) {
        throw new RuntimeException(
          "Can't parse hit content field " + field + " into class" + targetClass, e);
      }
    }

    if (facetConfigurations!=null) {
      List<Facet> facets = Lists.newArrayList();
      for (FacetConfiguration facetConfiguration : facetConfigurations) {
        List<AggregationConverter> converters = aggregationRegistry.getPluginsFor(this);
        Optional<AggregationConverter> converter = converters.stream().filter(c -> c.name().equals(facetConfiguration.getName())).findAny();
        if (converter.isPresent()) {
          if(facetConfiguration.isGlobal()){
            facets.add(converter.get().decode(((Global)searchResponse.getAggregations().get(GLOBAL_AGGREGATION))::getAggregations, facetConfiguration));
          }else{
            facets.add(converter.get().decode(searchResponse::getAggregations, facetConfiguration));
          }
        }
      }

      return new SearchResult(
        searchResponse.getTookInMillis(),
        new PageImpl<>(resultList, pageable, searchResponse.getHits().getTotalHits()),
        facets);
    }

    return new SearchResult(
      searchResponse.getTookInMillis(),
      new PageImpl<>(resultList, pageable, searchResponse.getHits().getTotalHits()));
  }

  @Override
  public boolean supports(Class<? extends AbstractDTO> delimiter) {
    return delimiter.isAssignableFrom(this.configuration.getSupportedClass());
  }

}
