/**
 *
 */
package fr.mgargadennec.blossom.core.common.support.history.reader.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.AndFilterBuilder;
import org.elasticsearch.index.query.FilterBuilder;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermFilterBuilder;
import org.elasticsearch.index.query.TermsFilterBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.common.constants.BlossomConst;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomResourceType;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryAssociationDTO;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryDTO;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryLinkedEntityDTO;
import fr.mgargadennec.blossom.core.common.support.history.reader.BlossomHistorySearchFilter;
import fr.mgargadennec.blossom.core.common.support.history.reader.IBlossomHistoryReader;

/**
 * @author pporcher
 *
 */
@BlossomResourceType(BlossomConst.BLOSSOM_HISTORY_ENTITY_NAME)
public class BlossomHistoryReaderImpl implements IBlossomHistoryReader {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlossomHistoryReaderImpl.class);

  private static final String HISTORY_ALIAS = "history";

  private Client esClient;

  private ObjectMapper objectMapper;

  private static final String REVISION_TYPE_ASSO = "ASSO";

  /**
   * @param esClient
   */
  public BlossomHistoryReaderImpl(Client esClient, ObjectMapper objectMapper) {
    super();
    this.esClient = esClient;
    this.objectMapper = objectMapper;
  }

  public Page<BlossomHistoryDTO<? extends BlossomAbstractEntity>> search(String queryString, BlossomHistorySearchFilter historyFilter,
      Pageable pageable) {

    QueryBuilder query;
    query = getQueryBuilder(queryString, historyFilter);

    SearchRequestBuilder searchRequest = null;

    searchRequest = esClient.prepareSearch(HISTORY_ALIAS).setQuery(query).setSize(pageable.getPageSize())
        .setFrom(pageable.getOffset());

    Sort sort = pageable.getSort();
    if (sort != null) {
      for (Order order : pageable.getSort()) {
        searchRequest.addSort(SortBuilders.fieldSort(order.getProperty()).order(
            SortOrder.valueOf(order.getDirection().name())));
      }
      searchRequest.addSort(SortBuilders.scoreSort());
    }

    LOGGER.debug(searchRequest.toString());

    SearchResponse searchResponse = searchRequest.get();

    List<BlossomHistoryDTO<? extends BlossomAbstractEntity>> histories = Lists.newArrayList();
    for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
      SearchHit hit = searchResponse.getHits().getHits()[i];
      histories.add(parseHistoryHitContent(hit.getSource(), hit.getSourceAsString()));
    }

    return new PageImpl<BlossomHistoryDTO<? extends BlossomAbstractEntity>>(histories, pageable, searchResponse.getHits()
        .getTotalHits());
  }

  public BlossomHistoryDTO<? extends BlossomAbstractEntity> getOne(String historyIdAsString) {
    GetResponse getResponse = esClient.prepareGet(HISTORY_ALIAS, null, historyIdAsString).get();

    return parseHistoryHitContent(getResponse.getSource(), getResponse.getSourceAsString());
  }

  private BlossomHistoryDTO<? extends BlossomAbstractEntity> parseHistoryHitContent(Map<String, Object> source,
      String sourceAsString) {
    try {
      Class<?> clazz = Class.forName((String) source.get("entityClass"));
     
      if (source.containsKey("association")) {
        Class<?> masterClazz = Class.forName((String) source.get("masterClass"));
        Class<?> slaveClazz = Class.forName((String) source.get("slaveClass"));
        return objectMapper.readValue(
            sourceAsString,
            objectMapper.getTypeFactory().constructParametrizedType(BlossomHistoryAssociationDTO.class,
            		BlossomHistoryAssociationDTO.class, clazz, masterClazz, slaveClazz));
      } else if (source.containsKey("linked")) {
        Class<?> linkedClazz = Class.forName((String) source.get("linkedClass"));
        return objectMapper.readValue(
            sourceAsString,
            objectMapper.getTypeFactory().constructParametrizedType(BlossomHistoryLinkedEntityDTO.class,
            		BlossomHistoryLinkedEntityDTO.class, clazz, linkedClazz));
      } else {
        return objectMapper.readValue(sourceAsString,
            objectMapper.getTypeFactory().constructParametrizedType(BlossomHistoryDTO.class, BlossomHistoryDTO.class, clazz));
      }
    } catch (IOException e) {
      throw new RuntimeException("Can't parse history hit content", e);
    } catch (ClassNotFoundException e) {
      throw new RuntimeException("Can't parse history hit content : class not found", e);
    }
  }

  private QueryBuilder getQueryBuilder(String queryString, BlossomHistorySearchFilter historyFilter) {
    // Query String
    QueryBuilder queryStringQuery;
    if (StringUtils.isEmpty(queryString)) {
      queryStringQuery = QueryBuilders.matchAllQuery();
    } else {
      queryStringQuery = QueryBuilders
          .boolQuery()
          .should(
              QueryBuilders
                  .multiMatchQuery(queryString, "entityId", "revisionArgs", "userModification", "userCreation",
                      "diff.left", "diff.right").type(Type.CROSS_FIELDS).lenient(true))
          .should(QueryBuilders.matchQuery("_all", queryString));
    }

    // Filter
    FilterBuilder filter;
    filter = getFilterBuilder(historyFilter);

    // Result
    return QueryBuilders.filteredQuery(queryStringQuery, filter);
  }

  private FilterBuilder getFilterBuilder(BlossomHistorySearchFilter historyFilter) {
    if (historyFilter == null) {
      return FilterBuilders.matchAllFilter();
    }

    List<FilterBuilder> filterBuilderList = new ArrayList<FilterBuilder>();

    // Entity Name
    if (!ArrayUtils.isEmpty(historyFilter.getEntityName())) {
      String[] entityNames = historyFilter.getEntityName();

      // Le nom de l'entity de base
      TermsFilterBuilder filterBaseEntityName = FilterBuilders.termsFilter("entityName", entityNames);

      // Le nom de l'entity dans les associations en tant que slave ou master
      TermsFilterBuilder filterMasterEntityName = FilterBuilders.termsFilter("masterName", entityNames);
      TermsFilterBuilder filterSlaveEntityName = FilterBuilders.termsFilter("slaveName", entityNames);
      TermsFilterBuilder filterLinkedEntityName = FilterBuilders.termsFilter("linkedName", entityNames);

      filterBuilderList.add(FilterBuilders.orFilter(filterBaseEntityName, filterSlaveEntityName,
          filterMasterEntityName, filterLinkedEntityName));
    }

    // Entity Id
    if (!StringUtils.isEmpty(historyFilter.getEntityId())) {
      // L'entity id dans une entite de base
      TermFilterBuilder filterBaseEntityId = FilterBuilders.termFilter("entityId", historyFilter.getEntityId());

      // L'entity id dans une entite d'association
      TermFilterBuilder isAssociationFilterAssociation = FilterBuilders.termFilter("association", true);
      TermFilterBuilder termFilterLeft = FilterBuilders.termFilter("diff.left", historyFilter.getEntityId());
      TermFilterBuilder termFilterRight = FilterBuilders.termFilter("diff.right", historyFilter.getEntityId());
      TermFilterBuilder termFilterMaster = FilterBuilders.termFilter("masterEntity.id", historyFilter.getEntityId());
      TermFilterBuilder termFilterSlave = FilterBuilders.termFilter("slaveEntity.id", historyFilter.getEntityId());

      AndFilterBuilder filterAssociationEntityId = FilterBuilders.andFilter(isAssociationFilterAssociation,
          FilterBuilders.orFilter(termFilterLeft, termFilterRight, termFilterMaster, termFilterSlave));

      // L'entity id est dans une entit�e li�e
      TermFilterBuilder termFilterLinked = FilterBuilders.termFilter("linkedEntity.id", historyFilter.getEntityId());

      filterBuilderList.add(FilterBuilders.orFilter(filterBaseEntityId, filterAssociationEntityId, termFilterLinked));
    }

    // User Creation
    if (!StringUtils.isEmpty(historyFilter.getUserCreation())) {
      filterBuilderList.add(FilterBuilders.termFilter("userCreation.exact", historyFilter.getUserCreation()));
    }

    // User Modification
    if (!StringUtils.isEmpty(historyFilter.getUserModification())) {
      filterBuilderList.add(FilterBuilders.termFilter("userModification.exact", historyFilter.getUserModification()));
    }

    // Revision Type
    if (!ArrayUtils.isEmpty(historyFilter.getRevisionType())) {
      boolean associations = ArrayUtils.contains(historyFilter.getRevisionType(), REVISION_TYPE_ASSO);

      // everything except ASSO
      String[] revisionTypesNotAsso = historyFilter.getRevisionType();
      ArrayUtils.removeElement(revisionTypesNotAsso, REVISION_TYPE_ASSO);

      // ADD, MOD, DEL on entity but not associations
      FilterBuilder filterRevisionTypeNotAsso = FilterBuilders.termsFilter("revisionType", revisionTypesNotAsso);
      FilterBuilder filterAssociationFalse = FilterBuilders.termFilter("association", false);
      FilterBuilder filterRevisionTypeNonAsso = FilterBuilders.andFilter(filterRevisionTypeNotAsso,
          filterAssociationFalse);

      if (associations) {
        // both entities + associations
        FilterBuilder termFilterAssociationTrue = FilterBuilders.termFilter("association", true);
        filterBuilderList.add(FilterBuilders.orFilter(filterRevisionTypeNonAsso, termFilterAssociationTrue));
      } else {
        // entities but not associations
        filterBuilderList.add(filterRevisionTypeNonAsso);
      }
    }

    return FilterBuilders.andFilter(filterBuilderList.toArray(new FilterBuilder[filterBuilderList.size()]));
  }
}
