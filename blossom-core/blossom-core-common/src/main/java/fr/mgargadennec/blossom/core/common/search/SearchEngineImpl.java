package fr.mgargadennec.blossom.core.common.search;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;

import java.io.IOException;
import java.util.List;

public class SearchEngineImpl<DTO extends AbstractDTO> implements SearchEngine {

  private final Class<DTO> supportedClass;
  private final Client client;
  private final List<String> fields;
  private final String alias;
  private final ObjectMapper objectMapper;

  public SearchEngineImpl(Class<DTO> supportedClass, Client client, List<String> fields, String alias, ObjectMapper objectMapper) {
    this.supportedClass = supportedClass;
    this.client = client;
    this.fields = fields;
    this.alias = alias;
    this.objectMapper = objectMapper;
  }

  public Page<DTO> search(String q, Pageable pageable) {
    return this.search(q, pageable, null);
  }

  @Override
  public Page<DTO> search(String q, Pageable pageable, Iterable<QueryBuilder> filters) {
    QueryBuilder initialQuery;
    String[] searchableFields = this.searchableFields();

    if (Strings.isNullOrEmpty(q)) {
      initialQuery = QueryBuilders.matchAllQuery();
    } else if (searchableFields == null) {
      initialQuery = QueryBuilders.simpleQueryStringQuery(q);
    } else {
      initialQuery = QueryBuilders.multiMatchQuery(q, searchableFields).type(Type.CROSS_FIELDS).lenient(true);
    }

    BoolQueryBuilder query = QueryBuilders.boolQuery().must(initialQuery);

    if (filters != null) {
      for (QueryBuilder filter : filters) {
        query = query.filter(filter);
      }
    }

    SearchRequestBuilder searchRequest = this.client.prepareSearch(this.alias).setQuery(query).setSize(pageable.getPageSize()).setFrom(pageable.getOffset());

    Sort sort = pageable.getSort();
    if (sort != null) {
      for (Order order : pageable.getSort()) {
        searchRequest.addSort(SortBuilders.fieldSort(order.getProperty()).order(SortOrder.valueOf(order.getDirection().name())));
      }
      searchRequest.addSort(SortBuilders.scoreSort());
    }

    List<DTO> resultList = Lists.newArrayList();

    SearchResponse searchResponse = searchRequest.get();
    for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
      try {
        SearchHit hit = searchResponse.getHits().getHits()[i];
        DTO result = this.objectMapper.readValue(hit.getSourceAsString(), this.supportedClass);
        resultList.add(result);
      } catch (IOException e) {
        throw new RuntimeException("Can't parse hit content into supported class" + this.supportedClass, e);
      }
    }

    return new PageImpl<>(resultList, pageable, searchResponse.getHits().getTotalHits());
  }

  private String[] searchableFields() {
    if (this.fields == null) {
      return null;
    }

    return this.fields.toArray(new String[this.fields.size()]);
  }

  @Override
  public boolean supports(Class<? extends AbstractDTO> delimiter) {
    return delimiter.isAssignableFrom(this.supportedClass);
  }

}
