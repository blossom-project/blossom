package fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl;

import java.util.List;
import java.util.Map;

import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.Requests;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.hibernate.envers.RevisionType;
import org.hibernate.envers.tools.Pair;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.base.Strings;

import fr.mgargadennec.blossom.core.common.dao.history.IBlossomHistoryDAO;
import fr.mgargadennec.blossom.core.common.dao.history.IBlossomRevisionDAO;
import fr.mgargadennec.blossom.core.common.model.common.BlossomAbstractEntity;
import fr.mgargadennec.blossom.core.common.model.common.BlossomRevisionEntity;
import fr.mgargadennec.blossom.core.common.support.history.BlossomHistoryDTO;
import fr.mgargadennec.blossom.core.common.support.history.builder.IBlossomHistoryBuilder;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryIncoherentDataException;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryIncompleteDataException;
import fr.mgargadennec.blossom.core.common.support.history.builder.exception.BlossomHistoryTechnicalException;

public class BlossomScheduledHistoryExtractor {

  private static final Logger LOGGER = LoggerFactory.getLogger(BlossomScheduledHistoryExtractor.class);

  private static String ALIAS = "history";

  private Client esClient;
  private boolean enabled;
  private IBlossomHistoryDAO historyDao;
  private IBlossomRevisionDAO blossomRevisionDao;
  private PluginRegistry<IBlossomHistoryBuilder, Class<? extends BlossomAbstractEntity>> historyPluginRegistry;
  private String settings;

  private Object lock = new Object();

  private ObjectMapper objectMapper = new ObjectMapper();

  protected BlossomScheduledHistoryExtractor() {
  }

  public BlossomScheduledHistoryExtractor(Client esClient, boolean enabled, IBlossomHistoryDAO historyDao,
      IBlossomRevisionDAO blossomRevisionDao,
      PluginRegistry<IBlossomHistoryBuilder, Class<? extends BlossomAbstractEntity>> historyBuilderFactory, String settings) {
    super();
    this.esClient = esClient;
    this.enabled = enabled;
    this.historyDao = historyDao;
    this.historyPluginRegistry = historyBuilderFactory;
    this.blossomRevisionDao = blossomRevisionDao;
    this.settings = settings;
  }

  @Scheduled(fixedDelay = 1000 * 60 * 60, initialDelay = 1000 * 20)
  public void indexFull() {
    if (!enabled) {
      return;
    }

    synchronized (lock) {
      final String indexName = ALIAS + "_" + DateTime.now().getMillis();
      CreateIndexRequestBuilder prepareCreate = esClient.admin().indices().prepareCreate(indexName);

      if (!Strings.isNullOrEmpty(settings)) {
        prepareCreate.setSource(settings);
      }

      CreateIndexResponse createIndexResponse = prepareCreate.get();
      if (!createIndexResponse.isAcknowledged()) {
        throw new IllegalStateException("Error creating index for Blossom " + ALIAS);
      }

      BlossomIndexationHistoryMap indexMap = new BlossomIndexationHistoryMap();

      // Wrap with security ? (begin)

      try {
        // Liste des revisions en commencant par la derniere
        Sort descSort = new Sort(Direction.DESC, "id");
        List<BlossomRevisionEntity> revisionList = blossomRevisionDao.getAll(descSort);
        int total = 0;

        // Parcours de l'ensemble des revisions de la plus recente a la plus ancienne
        // en faisant un diff lorsqu'on rencontre 2 fois le meme objet
        for (BlossomRevisionEntity revision : revisionList) {
          total += indexRevision(indexName, indexMap, revision);
        }

        // Parcours de la liste des objets en modification dont on a pas trouve l'etat de l'objet avant la modification
        total += indexRemainingModification(indexName, indexMap);

        LOGGER.info("Indexation FULL des " + ALIAS + " --> " + total);

        IndicesAliasesRequestBuilder aliasBuilder = esClient.admin().indices().prepareAliases();
        String oldIndexName = getIndexNameFromAliasName(ALIAS);
        if (oldIndexName != null) {
          aliasBuilder.removeAlias(oldIndexName, ALIAS);
        }
        aliasBuilder.addAlias(indexName, ALIAS);
        aliasBuilder.get();

        if (oldIndexName != null) {
          esClient.admin().indices().prepareDelete(oldIndexName).get();
        }
      } catch (Exception e) {
        esClient.admin().indices().prepareDelete(indexName).get();
        LOGGER.error("Impossible d'indexer les " + ALIAS, e);
      }
      // Wrap with security ? (end)
    }
  }

  @Async
  public void indexOne(final BlossomRevisionEntity revision) {
    if (!enabled || revision == null) {
      return;
    }

    synchronized (lock) {
      final String indexName = getIndexNameFromAliasName(ALIAS);

      Map<RevisionType, List<BlossomAbstractEntity>> entityListGroupByRevisionType = historyDao
          .getEntityListGroupByRevisionType(revision.getId());

      try {
        for (BlossomAbstractEntity delEntity : entityListGroupByRevisionType.get(RevisionType.DEL)) {
          indexEntity(indexName, RevisionType.DEL, revision, delEntity, null);
        }

        for (BlossomAbstractEntity modEntity : entityListGroupByRevisionType.get(RevisionType.MOD)) {
          Class<? extends BlossomAbstractEntity> blossomEntityClazz = modEntity.getClass();

          BlossomAbstractEntity beforeMODEntity = historyDao.getEntityAtPreviousRevision(blossomEntityClazz,
              modEntity.getId(), revision.getId());

          indexEntity(indexName, RevisionType.MOD, revision, beforeMODEntity, modEntity);
        }

        for (BlossomAbstractEntity addEntity : entityListGroupByRevisionType.get(RevisionType.ADD)) {
          indexEntity(indexName, RevisionType.ADD, revision, null, addEntity);
        }
      } catch (Exception e) {
        LOGGER.error("Impossible d'indexer l'historique de la revision " + revision.getId(), e);
      }
    }
  }

  /**
   * Index une revision pour l'indexation complete.
   *
   * Retourne le nombre de documents cr��s.
   *
   * @param indexName
   * @param indexMap
   * @param revision
   * @return
   * @throws BlossomHistoryTechnicalException
   * @throws JsonProcessingException
   */
  private int indexRevision(String indexName, BlossomIndexationHistoryMap indexMap, BlossomRevisionEntity revision)
      throws BlossomHistoryTechnicalException, JsonProcessingException {
    int total = 0;

    try {
      Map<RevisionType, List<BlossomAbstractEntity>> entityListGroupByRevisionType = historyDao
          .getEntityListGroupByRevisionType(revision.getId());

      for (BlossomAbstractEntity delEntity : entityListGroupByRevisionType.get(RevisionType.DEL)) {
        total += indexEntity(indexName, RevisionType.DEL, revision, delEntity, null);
      }

      for (BlossomAbstractEntity modEntity : entityListGroupByRevisionType.get(RevisionType.MOD)) {
        Pair<BlossomRevisionEntity, BlossomAbstractEntity> afterMODPair = indexMap.pull(modEntity.getClass(),
            modEntity.getId());

        if (afterMODPair != null) {
          BlossomRevisionEntity afterMODRevision = afterMODPair.getFirst();
          BlossomAbstractEntity afterMODEntity = afterMODPair.getSecond();

          total += indexEntity(indexName, RevisionType.MOD, afterMODRevision, modEntity, afterMODEntity);
        }

        indexMap.push(revision, modEntity);
      }

      for (BlossomAbstractEntity addEntity : entityListGroupByRevisionType.get(RevisionType.ADD)) {
        Pair<BlossomRevisionEntity, BlossomAbstractEntity> afterMODPair = indexMap.pull(addEntity.getClass(),
            addEntity.getId());

        if (afterMODPair != null) {
          BlossomRevisionEntity afterMODRevision = afterMODPair.getFirst();
          BlossomAbstractEntity afterMODEntity = afterMODPair.getSecond();
          total += indexEntity(indexName, RevisionType.MOD, afterMODRevision, addEntity, afterMODEntity);
        }

        total += indexEntity(indexName, RevisionType.ADD, revision, null, addEntity);
      }
    } catch (Exception e) {
      LOGGER.warn("Unable to index revision " + revision.getId(), e);
    }

    return total;
  }

  /**
   * Index les objets en modification dont on a pas trouve l'etat de l'objet avant la modification.
   *
   * Retourne le nombre de documents cr��s
   *
   * @param indexName
   * @param indexMap
   * @return
   * @throws JsonProcessingException
   * @throws BlossomHistoryTechnicalException
   */
  private int indexRemainingModification(String indexName, BlossomIndexationHistoryMap indexMap)
      throws BlossomHistoryTechnicalException, JsonProcessingException {
    int total = 0;

    for (Pair<BlossomRevisionEntity, BlossomAbstractEntity> remainingMODPair : indexMap.getRemainingEntityList()) {
      BlossomRevisionEntity afterMODRevision = remainingMODPair.getFirst();
      BlossomAbstractEntity afterMODEntity = remainingMODPair.getSecond();

      total += indexEntity(indexName, RevisionType.MOD, afterMODRevision, null, afterMODEntity);
    }

    return total;
  }

  private int indexEntity(String indexName, RevisionType revisionType, BlossomRevisionEntity revision,
      BlossomAbstractEntity beforeEntity, BlossomAbstractEntity afterEntity) throws BlossomHistoryTechnicalException,
      JsonProcessingException {
    BlossomHistoryDTO<?> historyDTO = null;

    try {
      switch (revisionType) {
      case ADD:
        historyDTO = historyPluginRegistry.getPluginFor(afterEntity.getClass()).buildDTOfromADD(revision, afterEntity);
        break;
      case MOD:
        if (beforeEntity != null) {
          historyDTO = historyPluginRegistry.getPluginFor(afterEntity.getClass()).buildDTOfromMOD(revision,
              beforeEntity, afterEntity);
        } else {
          historyDTO = historyPluginRegistry.getPluginFor(afterEntity.getClass())
              .buildDTOfromMOD(revision, afterEntity);
        }
        break;
      case DEL:
        historyDTO = historyPluginRegistry.getPluginFor(beforeEntity.getClass())
        .buildDTOfromDEL(revision, beforeEntity);
        break;
      default:
        throw new BlossomHistoryTechnicalException("Given revisionType is invalid");
      }

      return indexHistoryDTO(indexName, historyDTO);

    } catch (BlossomHistoryIncompleteDataException e) {
      LOGGER.debug("An entity revision has been ignored due to incomplete data", e);
    } catch (BlossomHistoryIncoherentDataException e) {
      LOGGER.warn("An entity revision has been ignored due to incoherent data", e);
    }

    return 0;
  }

  private int indexHistoryDTO(String indexName, BlossomHistoryDTO<? extends BlossomAbstractEntity> historyDTO)
      throws JsonProcessingException {
    if (historyDTO == null) {
      return 0;
    }

    ObjectNode json = objectMapper.valueToTree(historyDTO);

    esClient.prepareUpdate(indexName, historyDTO.getEntityName(), historyDTO.getIdAsString()).setDocAsUpsert(true)
    .setDoc(objectMapper.writeValueAsString(json)).get();

    return 1;
  }

  private String getIndexNameFromAliasName(final String aliasName) {
    ImmutableOpenMap<String, AliasMetaData> indexToAliasesMap = esClient.admin().cluster()
        .state(Requests.clusterStateRequest()).actionGet().getState().getMetaData().aliases().get(aliasName);
    if (indexToAliasesMap != null && !indexToAliasesMap.isEmpty()) {
      return indexToAliasesMap.keys().iterator().next().value;
    }
    return null;
  }
}
