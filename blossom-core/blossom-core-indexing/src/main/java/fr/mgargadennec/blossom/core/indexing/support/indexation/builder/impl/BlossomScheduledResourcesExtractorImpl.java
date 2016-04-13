package fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import fr.mgargadennec.blossom.core.common.model.common.BlossomIdentifiable;
import fr.mgargadennec.blossom.core.common.service.BlossomAbstractServiceDTO;
import fr.mgargadennec.blossom.core.common.service.IBlossomServiceDTO;
import fr.mgargadennec.blossom.core.common.service.generic.IBlossomGenericReadService;
import fr.mgargadennec.blossom.core.common.support.BlossomAssociationMasterSlaveDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexInfosDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.BlossomIndexationAssociationDTO;
import fr.mgargadennec.blossom.core.common.support.indexation.builder.IBlossomScheduledResourcesExtractor;
import fr.mgargadennec.blossom.security.core.model.BlossomRightPermissionEnum;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

public class BlossomScheduledResourcesExtractorImpl<S extends IBlossomServiceDTO> implements IBlossomScheduledResourcesExtractor {
  private static final Logger LOGGER = LoggerFactory.getLogger(BlossomScheduledResourcesExtractorImpl.class);
  private Client esClient;
  private String alias;
  private IBlossomGenericReadService<?, S> service;
  private Class<S> supportedClazz;
  private String settings;
  private IBlossomAuthenticationUtilService boAuthenticationUtilService;
  private String indexedResourceType;
  private List<BlossomIndexationAssociationDTO<? extends BlossomAbstractServiceDTO>> indexationAssociations;

  private ObjectMapper objectMapper = new ObjectMapper();

  public BlossomScheduledResourcesExtractorImpl(Client esClient, 
      BlossomIndexInfosDTO boIndexInfosDTO, IBlossomGenericReadService<?, S> service, Class<S> supportedClazz,
      String settings, IBlossomAuthenticationUtilService boAuthenticationUtilService,
      List<BlossomIndexationAssociationDTO<? extends BlossomAbstractServiceDTO>> indexationAssociations) {
    this.esClient = esClient;
    this.alias = boIndexInfosDTO.getAlias();
    this.service = service;
    this.supportedClazz = supportedClazz;
    this.settings = settings;
    this.boAuthenticationUtilService = boAuthenticationUtilService;
    this.indexedResourceType = boIndexInfosDTO.getIndexedResourceType();
    this.indexationAssociations = indexationAssociations;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 5, initialDelay = 10000)
  public void indexFull() throws JsonProcessingException {
    final String indexName = alias + "_" + DateTime.now().getMillis();
    CreateIndexRequestBuilder prepareCreate = esClient.admin().indices().prepareCreate(indexName);

    if (!Strings.isNullOrEmpty(settings)) {
      prepareCreate.setSource(settings);
    }

    CreateIndexResponse createIndexResponse = prepareCreate.get();
    if (!createIndexResponse.isAcknowledged()) {
      throw new IllegalStateException("Error creating index for Blossom");
    }

    boAuthenticationUtilService.runAs(new Runnable() {

      @SuppressWarnings("unchecked")
      public void run() {
        try {
          Pageable pageable = new PageRequest(0, 1000);
          boolean hasMoreElements = true;
          long total = 0;
          do {

            Map<Long, List<Long>> authorizedGroupIds = getAuthorizations();

            Map<String, Map<Long, List<Long>>> associatedIdsByRelationName = getAllAssociationsByName();

            Page<S> pagedEntity = service.getAll(pageable);
            total = pagedEntity.getTotalElements();
            List<S> entities = pagedEntity.getContent();
            List<BlossomIdentifiable<Long>> identifiableEntities = new ArrayList<BlossomIdentifiable<Long>>(entities);
            indexEntities(indexName, identifiableEntities, associatedIdsByRelationName, authorizedGroupIds);

            if (pagedEntity.hasNext()) {
              pageable = pagedEntity.nextPageable();
            } else {
              hasMoreElements = false;
            }

          } while (hasMoreElements);

          LOGGER.info("Indexation FULL des " + alias + " --> " + total);

          IndicesAliasesRequestBuilder aliasBuilder = esClient.admin().indices().prepareAliases();
          String oldIndexName = getIndexNameFromAliasName(alias);
          if (oldIndexName != null) {
            aliasBuilder.removeAlias(oldIndexName, alias);
          }
          aliasBuilder.addAlias(indexName, alias);
          aliasBuilder.get();

          if (oldIndexName != null) {
            esClient.admin().indices().prepareDelete(oldIndexName).get();
          }
        } catch (Exception e) {
          esClient.admin().indices().prepareDelete(indexName).get();
          LOGGER.error("Impossible d'indexer les " + alias, e);
        }
      }
    }, false, true, boAuthenticationUtilService.rightsForEntity(indexedResourceType, BlossomRightPermissionEnum.READ,
        BlossomRightPermissionEnum.CLEARANCE));

  }

  public void indexOne(final BlossomIdentifiable<Long> entity) {
    if (entity == null) {
      return;
    }
    boAuthenticationUtilService.runAs(new Runnable() {
      public void run() {
        try {
          String indexName = getIndexNameFromAliasName(alias);

          Map<String, Map<Long, List<Long>>> associatedIdsByRelationName = getAllAssociationsByName();
          Map<Long, List<Long>> authorizedGroupIds = getAuthorizations();

          if (!Strings.isNullOrEmpty(indexName)) {
            indexEntities(indexName, Lists.newArrayList(entity), associatedIdsByRelationName, authorizedGroupIds);
          }
        } catch (Exception e) {
          LOGGER.error("Impossible d'indexer l'entit� " + alias + " dont l'id est " + entity.getId(), e);
        }

      }
    }, false, true, boAuthenticationUtilService.rightsForEntity(indexedResourceType, BlossomRightPermissionEnum.READ,
        BlossomRightPermissionEnum.CLEARANCE));

  }

  public void removeOne(final Long entityId) {
    if (entityId == null) {
      return;
    }
    boAuthenticationUtilService.runAs(new Runnable() {
      public void run() {
        try {
          String indexName = getIndexNameFromAliasName(alias);

          if (!Strings.isNullOrEmpty(indexName)) {
            removeEntities(indexName, Lists.newArrayList(entityId));
          }
        } catch (Exception e) {
          LOGGER.error("Impossible de supprimer l'entit� " + alias + " dont l'id est " + entityId, e);
        }

      }
    }, false, true, boAuthenticationUtilService.rightsForEntity(indexedResourceType, BlossomRightPermissionEnum.READ));

  }

  private Map<String, Map<Long, List<Long>>> getAllAssociationsByName() {

    if (indexationAssociations == null) {
      return new HashMap<String, Map<Long, List<Long>>>();
    }

    Map<String, Map<Long, List<Long>>> associatedIdsByEntityIds = new HashMap<String, Map<Long, List<Long>>>();

    for (BlossomIndexationAssociationDTO<? extends BlossomAbstractServiceDTO> indexationAssociation : indexationAssociations) {

      Map<Long, List<Long>> idsForEntityIds = new HashMap<Long, List<Long>>();

      Authentication previousAuthentication = boAuthenticationUtilService.beginAs(false, true,
          boAuthenticationUtilService.rightsForEntity(indexationAssociation.getAssociationResourceType(),
              BlossomRightPermissionEnum.READ));
      List<BlossomAssociationMasterSlaveDTO> allAssociations = indexationAssociation.getAllAssociations();
      boAuthenticationUtilService.endAs(previousAuthentication);

      for (BlossomAssociationMasterSlaveDTO associationMasterSlaveDTO : allAssociations) {

        List<Long> ids = idsForEntityIds.get(associationMasterSlaveDTO.getMasterId());
        if (ids == null) {
          ids = new ArrayList<Long>();
        }
        ids.add(associationMasterSlaveDTO.getSlaveId());
        idsForEntityIds.put(associationMasterSlaveDTO.getMasterId(), ids);

      }

      associatedIdsByEntityIds.put(indexationAssociation.getAssociationName(), idsForEntityIds);

    }

    return associatedIdsByEntityIds;
  }

  private Map<Long, List<Long>> getAuthorizations() {
    Map<Long, List<Long>> result = null;
    return result;
  }

  private void indexEntities(String indexName, List<BlossomIdentifiable<Long>> entities,
      Map<String, Map<Long, List<Long>>> associatedIdsByRelationName, Map<Long, List<Long>> authorizedGroupIds)
      throws JsonProcessingException {
    if (entities.isEmpty()) {
      return;
    }

    BulkRequestBuilder bulkRequest = esClient.prepareBulk();
    for (final BlossomIdentifiable<Long> entity : entities) {

      ObjectNode json = objectMapper.valueToTree(entity);
      ObjectNode associations = json.putObject("associations");
      for (Entry<String, Map<Long, List<Long>>> entry : associatedIdsByRelationName.entrySet()) {
        List<Long> associatedIds = entry.getValue().get(entity.getId());
        if (associatedIds == null) {
          associatedIds = new ArrayList<Long>();
        }
        associations.putArray(entry.getKey()).add(objectMapper.valueToTree(associatedIds));
      }

      if (authorizedGroupIds != null) {
        ArrayNode authorizations = json.putArray("authorizations");
        List<Long> groupIds = authorizedGroupIds.get(entity.getId());
        if (groupIds == null) {
          groupIds = new ArrayList<Long>();
        }
        authorizations.add(objectMapper.valueToTree(groupIds));
      }
      bulkRequest.add(esClient.prepareUpdate(indexName, alias, entity.getId().toString()).setDocAsUpsert(true)
          .setDoc(objectMapper.writeValueAsString(json)));
    }
    bulkRequest.get();
  }

  private void removeEntities(String indexName, List<Long> entityIds) {
    if (entityIds.isEmpty()) {
      return;
    }

    BulkRequestBuilder bulkRequest = esClient.prepareBulk();
    for (final Long entityId : entityIds) {
      bulkRequest.add(esClient.prepareDelete(indexName, alias, entityId.toString()));
    }
    bulkRequest.get();
  }

  private String getIndexNameFromAliasName(final String aliasName) {
    ImmutableOpenMap<String, AliasMetaData> indexToAliasesMap = esClient.admin().cluster()
        .state(Requests.clusterStateRequest()).actionGet().getState().getMetaData().aliases().get(aliasName);
    if (indexToAliasesMap != null && !indexToAliasesMap.isEmpty()) {
      return indexToAliasesMap.keys().iterator().next().value;
    }
    return null;
  }

  public boolean supports(Class<?> cls) {
    return this.supportedClazz.isAssignableFrom(cls);
  }

}