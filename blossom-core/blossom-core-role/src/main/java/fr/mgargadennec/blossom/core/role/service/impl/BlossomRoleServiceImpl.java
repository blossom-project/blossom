package fr.mgargadennec.blossom.core.role.service.impl;

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
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.IBlossomElasticsearchQueryService;
import fr.mgargadennec.blossom.core.role.constants.BlossomRoleConst;
import fr.mgargadennec.blossom.core.role.process.IBlossomRoleProcess;
import fr.mgargadennec.blossom.core.role.process.dto.BlossomRoleProcessDTO;
import fr.mgargadennec.blossom.core.role.service.IBlossomRoleService;
import fr.mgargadennec.blossom.core.role.service.dto.BlossomRoleServiceDTO;
import fr.mgargadennec.blossom.security.core.service.impl.BlossomSecuredGenericServiceImpl;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomRoleServiceImpl extends
    BlossomSecuredGenericServiceImpl<BlossomRoleProcessDTO, BlossomRoleServiceDTO> implements IBlossomRoleService {
  private static final Logger LOGGER = LoggerFactory.getLogger(BlossomRoleServiceImpl.class);

  private IBlossomRoleProcess boRoleProcess;
  private IBlossomElasticsearchQueryService esService;

  private IBlossomESResourceIndexBuilder boRoleESResourceIndexBuilder;

  public BlossomRoleServiceImpl(IBlossomRoleProcess boRoleProcess, IBlossomElasticsearchQueryService esService,
      IBlossomAuthenticationUtilService boAuthenticationUtilService, ApplicationEventPublisher eventPublisher,
      IBlossomESResourceIndexBuilder boRoleESResourceIndexBuilder) {
    super(boRoleProcess, boAuthenticationUtilService, eventPublisher);
    this.boRoleProcess = boRoleProcess;
    this.esService = esService;
    this.boRoleESResourceIndexBuilder = boRoleESResourceIndexBuilder;
  }

  public Page<BlossomRoleServiceDTO> search(String q, Pageable pageable) {
    QueryBuilder query = QueryBuilders.multiMatchQuery(q, "id", "name", "description").type(Type.CROSS_FIELDS)
        .lenient(true);

    SearchRequestBuilder searchRequest = esService.prepareSearchRequest(boRoleESResourceIndexBuilder.getIndexInfos(),
        query, pageable.getPageSize(), pageable.getOffset());

    Sort sort = pageable.getSort();
    if (sort != null) {
      for (Order order : pageable.getSort()) {
        searchRequest
            .addSort(SortBuilders.fieldSort(order.getProperty()).order(SortOrder.valueOf(order.getDirection().name())));
      }
      searchRequest.addSort(SortBuilders.scoreSort());
    }

    LOGGER.debug(searchRequest.toString());

    SearchResponse searchResponse = searchRequest.get();

    List<BlossomRoleServiceDTO> roles = Lists.newArrayList();
    for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
      try {
        roles.add(new ObjectMapper().readValue(searchResponse.getHits().getHits()[i].getSourceAsString(),
            BlossomRoleServiceDTO.class));
      } catch (IOException e) {
        throw new RuntimeException("Can't parse role hit content", e);
      }
    }

    return new PageImpl<BlossomRoleServiceDTO>(roles, pageable, searchResponse.getHits().getTotalHits());
  }

  public Page<BlossomRoleServiceDTO> search(String q, String associationName, String associationId, Pageable pageable) {

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

    SearchRequestBuilder searchRequest = esService.prepareSearchRequest(boRoleESResourceIndexBuilder.getIndexInfos(),
        queryFiltered, pageable.getPageSize(), pageable.getOffset());

    Sort sort = pageable.getSort();
    if (sort != null) {
      for (Order order : pageable.getSort()) {
        searchRequest
            .addSort(SortBuilders.fieldSort(order.getProperty()).order(SortOrder.valueOf(order.getDirection().name())));
      }
      searchRequest.addSort(SortBuilders.scoreSort());
    }

    LOGGER.debug(searchRequest.toString());

    SearchResponse searchResponse = searchRequest.get();

    List<BlossomRoleServiceDTO> roles = Lists.newArrayList();
    for (int i = 0; i < searchResponse.getHits().getHits().length; i++) {
      try {
        roles.add(new ObjectMapper().readValue(searchResponse.getHits().getHits()[i].getSourceAsString(),
            BlossomRoleServiceDTO.class));
      } catch (IOException e) {
        throw new RuntimeException("Can't parse role hit content", e);
      }
    }

    return new PageImpl<BlossomRoleServiceDTO>(roles, pageable, searchResponse.getHits().getTotalHits());
  }

  public BlossomRoleServiceDTO update(Long id, BlossomRoleServiceDTO boRoleServiceDTO) {
    BlossomRoleProcessDTO boProcessDTOToUpdate = boRoleProcess.get(id);

    boProcessDTOToUpdate.setName(boRoleServiceDTO.getName());
    boProcessDTOToUpdate.setDescription(boRoleServiceDTO.getDescription());

    BlossomRoleProcessDTO updated = boRoleProcess.update(boProcessDTOToUpdate);

    BlossomRoleServiceDTO result = createServiceDTOfromProcessDTO(updated);

    doPublishEvent(new BlossomEntityUpdatedEvent<BlossomRoleServiceDTO>(this, result));

    return result;
  }

  public BlossomRoleServiceDTO createServiceDTOfromProcessDTO(BlossomRoleProcessDTO i) {
    if (i == null) {
      return null;
    }

    BlossomRoleServiceDTO result = new BlossomRoleServiceDTO();
    result.setId(i.getId());
    result.setName(i.getName());
    result.setDescription(i.getDescription());
    result.setDateCreation(i.getDateCreation());
    result.setDateModification(i.getDateModification());
    result.setUserCreation(i.getUserCreation());
    result.setUserModification(i.getUserModification());

    return result;
  }

  public BlossomRoleProcessDTO createProcessDTOfromServiceDTO(BlossomRoleServiceDTO o) {
    if (o == null) {
      return null;
    }

    BlossomRoleProcessDTO result = new BlossomRoleProcessDTO();
    result.setId(o.getId());
    result.setName(o.getName());
    result.setDescription(o.getDescription());
    result.setDateCreation(o.getDateCreation());
    result.setDateModification(o.getDateModification());
    result.setUserCreation(o.getUserCreation());
    result.setUserModification(o.getUserModification());

    return result;
  }

  public boolean supports(String delimiter) {
    return BlossomRoleConst.BLOSSOM_ROLE_ENTITY_NAME.equals(delimiter);
  }

}
