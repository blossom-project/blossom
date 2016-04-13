package fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexInfosDTO;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomElasticsearchQueryServiceImpl implements IBlossomElasticsearchQueryService {
  private Client esClient;
  private IBlossomAuthenticationUtilService blossomAuthenticationUtilService;

  public BlossomElasticsearchQueryServiceImpl(Client esClient, IBlossomAuthenticationUtilService blossomAuthenticationUtilService) {
    this.esClient = esClient;
    this.blossomAuthenticationUtilService = blossomAuthenticationUtilService;
  }

  public SearchRequestBuilder prepareSearchRequest(BlossomIndexInfosDTO index, QueryBuilder query, int size, int offset) {
    return prepareSearchRequest(index, query, null, size, offset, true);
  }

  public SearchRequestBuilder prepareSearchRequest(BlossomIndexInfosDTO index, QueryBuilder query,
      BlossomSearchFilter searchFilter, int size, int offset) {
    return prepareSearchRequest(index, query, getFilterBuilder(searchFilter), size, offset, true);
  }

  public SearchRequestBuilder prepareSearchRequest(BlossomIndexInfosDTO index, QueryBuilder query,
      FilterBuilder otherFilter, int size, int offset) {
    return prepareSearchRequest(index, query, otherFilter, size, offset, true);
  }

  public SearchRequestBuilder prepareSearchRequest(BlossomIndexInfosDTO index, QueryBuilder query,
      FilterBuilder otherfilter, int size, int offset, boolean filter) {
    QueryBuilder filteredQuery = query;
    List<FilterBuilder> scopeFilters = new ArrayList<FilterBuilder>();

    if (index.isFiltered() && filter) {

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

      if (authentication != null) {
        if (!(blossomAuthenticationUtilService.isRoot(authentication) || blossomAuthenticationUtilService.hasGroup(
            authentication, BlossomConst.SECURITY_GROUP_ROOT_ID))) {
          scopeFilters.add(FilterBuilders.termFilter("userCreation", authentication.getName()));
          for (Long groupId : blossomAuthenticationUtilService.getGroupIds(authentication.getAuthorities())) {
            scopeFilters.add(FilterBuilders.termFilter("authorizations", groupId));
          }
        }
      }
    }

    List<FilterBuilder> computedFilters = Lists.newArrayList();
    if (!scopeFilters.isEmpty()) {
      computedFilters.add(FilterBuilders.orFilter(scopeFilters.toArray(new FilterBuilder[scopeFilters.size()])));
    }
    if (otherfilter != null) {
      computedFilters.add(otherfilter);
    }

    if (computedFilters.isEmpty()) {
      filteredQuery = query;
    } else {
      filteredQuery = QueryBuilders.filteredQuery(query,
          FilterBuilders.andFilter(computedFilters.toArray(new FilterBuilder[computedFilters.size()])));
    }

    return esClient.prepareSearch(index.getAlias()).setQuery(filteredQuery).setSize(size).setFrom(offset);

  }

  private FilterBuilder getFilterBuilder(BlossomSearchFilter searchFilter) {
    if (searchFilter == null || searchFilter.getFilters() == null) {
      return FilterBuilders.matchAllFilter();
    }

    List<FilterBuilder> filterBuilderList = new ArrayList<FilterBuilder>();

    for (Entry<String, Object> filterEntry : searchFilter.getFilters().entrySet()) {

      String key = filterEntry.getKey();
      Object value = filterEntry.getValue();

      if (value instanceof List) {
        FilterBuilder termsFilter = FilterBuilders.termsFilter(key, (List<?>) value);
        filterBuilderList.add(termsFilter);
      } else {
        FilterBuilder simpleFilter = FilterBuilders.termFilter(key, value);
        filterBuilderList.add(simpleFilter);
      }

    }
    return FilterBuilders.andFilter(filterBuilderList.toArray(new FilterBuilder[filterBuilderList.size()]));
  }

}
