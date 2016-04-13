package fr.mgargadennec.blossom.core.group.service.impl;

import java.io.IOException;
import java.util.List;

import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.FilterBuilders;
import org.elasticsearch.index.query.FilteredQueryBuilder;
import org.elasticsearch.index.query.MultiMatchQueryBuilder.Type;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Order;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.common.support.event.entity.BlossomEntityUpdatedEvent;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomESResourceIndexBuilder;
import fr.mgargadennec.blossom.core.group.constants.BlossomGroupConst;
import fr.mgargadennec.blossom.core.group.process.IBlossomGroupProcess;
import fr.mgargadennec.blossom.core.group.process.dto.BlossomGroupProcessDTO;
import fr.mgargadennec.blossom.core.group.service.IBlossomGroupService;
import fr.mgargadennec.blossom.core.group.service.dto.BlossomGroupServiceDTO;
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.IBlossomElasticsearchQueryService;
import fr.mgargadennec.blossom.security.core.service.impl.BlossomSecuredGenericServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomGroupServiceImpl extends BlossomSecuredGenericServiceImpl<BlossomGroupProcessDTO, BlossomGroupServiceDTO> implements
    IBlossomGroupService {
  private static final Logger LOGGER = LoggerFactory.getLogger(BlossomGroupServiceImpl.class);

  private IBlossomGroupProcess boGroupProcess;
//  private IBlossomAssociationGroupUserProcess boAssociationGroupUserProcess;
//  private IBlossomAssociationGroupEntityProcess boAssociationGroupEntityProcess;
  private IBlossomElasticsearchQueryService esService;

  private IBlossomESResourceIndexBuilder boGroupESResourceIndexBuilder;

  public BlossomGroupServiceImpl(IBlossomGroupProcess boGroupProcess,
      IBlossomElasticsearchQueryService esService,
      IBlossomAuthenticationUtilService boAuthenticationUtilService, ApplicationEventPublisher eventPublisher,
      IBlossomESResourceIndexBuilder boGroupESResourceIndexBuilder) {
    super(boGroupProcess, boAuthenticationUtilService, eventPublisher);
    this.boGroupProcess = boGroupProcess;
    this.esService = esService;
    this.boGroupESResourceIndexBuilder = boGroupESResourceIndexBuilder;
  }

  public Page<BlossomGroupServiceDTO> search(String q, Pageable pageable) {
    QueryBuilder query = QueryBuilders.multiMatchQuery(q, "id", "name", "description").type(Type.CROSS_FIELDS)
        .lenient(true);

    SearchRequestBuilder searchRequest = esService.prepareSearchRequest(boGroupESResourceIndexBuilder.getIndexInfos(),
        query, pageable.getPageSize(), pageable.getOffset());

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

    List<BlossomGroupServiceDTO> groups = Lists.newArrayList();
    for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
      try {
        groups.add(new ObjectMapper().readValue(searchResponse.getHits().getHits()[i].getSourceAsString(),
            BlossomGroupServiceDTO.class));
      } catch (IOException e) {
        throw new RuntimeException("Can't parse group hit content", e);
      }
    }

    return new PageImpl<BlossomGroupServiceDTO>(groups, pageable, searchResponse.getHits().getTotalHits());
  }

  public Page<BlossomGroupServiceDTO> search(String q, String associationName, String associationId, Pageable pageable) {

    QueryBuilder query = null;
    if (Strings.isNullOrEmpty(q)) {
      query = QueryBuilders.matchAllQuery();
    } else {
      query = QueryBuilders.multiMatchQuery(q, "id", "name", "description").type(Type.CROSS_FIELDS).lenient(true);
    }
    if (Strings.isNullOrEmpty(associationName) || Strings.isNullOrEmpty(associationId)) {
      return search(q, pageable);
    }

    FilteredQueryBuilder queryFiltered = QueryBuilders.filteredQuery(query,
        FilterBuilders.termsFilter("associations." + associationName, associationId));

    SearchRequestBuilder searchRequest = esService.prepareSearchRequest(boGroupESResourceIndexBuilder.getIndexInfos(),
        queryFiltered, pageable.getPageSize(), pageable.getOffset());

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

    List<BlossomGroupServiceDTO> groups = Lists.newArrayList();
    for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
      try {
        groups.add(new ObjectMapper().readValue(searchResponse.getHits().getHits()[i].getSourceAsString(),
            BlossomGroupServiceDTO.class));
      } catch (IOException e) {
        throw new RuntimeException("Can't parse group hit content", e);
      }
    }

    return new PageImpl<BlossomGroupServiceDTO>(groups, pageable, searchResponse.getHits().getTotalHits());
  }

  @Transactional
  public BlossomGroupServiceDTO update(Long groupId, BlossomGroupServiceDTO boGroupUpdateServiceDTO) {
    BlossomGroupProcessDTO boGroupProcessDTOtoUpdate = boGroupProcess.get(groupId);

    boGroupProcessDTOtoUpdate.setName(boGroupUpdateServiceDTO.getName());
    boGroupProcessDTOtoUpdate.setDescription(boGroupUpdateServiceDTO.getDescription());

    BlossomGroupProcessDTO updatedGroupProcessDTO = boGroupProcess.update(boGroupProcessDTOtoUpdate);

    BlossomGroupServiceDTO result = createServiceDTOfromProcessDTO(updatedGroupProcessDTO);

    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomGroupServiceDTO>(this, result));

    return result;
  }

  @Transactional
  public void delete(Long groupId) {
//    boAssociationGroupUserProcess.deleteByGroupId(groupId);
//    boAssociationGroupEntityProcess.deleteByGroupId(groupId);
    super.delete(groupId);
  }

  public BlossomGroupServiceDTO createServiceDTOfromProcessDTO(BlossomGroupProcessDTO i) {
    if (i == null) {
      return null;
    }

    BlossomGroupServiceDTO result = new BlossomGroupServiceDTO();
    result.setDateCreation(i.getDateCreation());
    result.setDateModification(i.getDateModification());
    result.setDescription(i.getDescription());
    result.setId(i.getId());
    result.setName(i.getName());
    result.setUserCreation(i.getUserCreation());
    result.setUserModification(i.getUserModification());

    return result;
  }

  public BlossomGroupProcessDTO createProcessDTOfromServiceDTO(BlossomGroupServiceDTO o) {
    if (o == null) {
      return null;
    }

    BlossomGroupProcessDTO result = new BlossomGroupProcessDTO();
    result.setDateCreation(o.getDateCreation());
    result.setDateModification(o.getDateModification());
    result.setDescription(o.getDescription());
    result.setId(o.getId());
    result.setName(o.getName());
    result.setUserCreation(o.getUserCreation());
    result.setUserModification(o.getUserModification());

    return result;
  }

  public boolean supports(String delimiter) {
    return BlossomGroupConst.BLOSSOM_GROUP_ENTITY_NAME.equals(delimiter);
  }
}
