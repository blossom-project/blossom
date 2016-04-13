package fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.QueryBuilder;

import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexInfosDTO;

public interface IBlossomElasticsearchQueryService {

  public SearchRequestBuilder prepareSearchRequest(BlossomIndexInfosDTO index, QueryBuilder query, int size, int offset);

  public SearchRequestBuilder prepareSearchRequest(BlossomIndexInfosDTO index, QueryBuilder query,
      BlossomSearchFilter searchFilter, int size, int offset);

  public SearchRequestBuilder prepareSearchRequest(BlossomIndexInfosDTO index, QueryBuilder query, FilterBuilder otherFilter,
      int size, int offset);

  public SearchRequestBuilder prepareSearchRequest(BlossomIndexInfosDTO index, QueryBuilder query, FilterBuilder otherfilter,
      int size, int offset, boolean filter);

}
