package fr.mgargadennec.blossom.core.common.search;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import fr.mgargadennec.blossom.core.common.dto.AbstractDTO;
import fr.mgargadennec.blossom.core.common.service.ReadOnlyService;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.function.Function;

public class IndexationEngineImpl<DTO extends AbstractDTO> implements IndexationEngine {
  private final static Logger logger = LoggerFactory.getLogger(IndexationEngineImpl.class);
  private final Class<DTO> supportedClass;
  private final Client client;
  private final Resource source;
  private final String alias;
  private final Function<DTO, String> typeFunction;
  private final ReadOnlyService<DTO> service;

  private final ObjectMapper objectMapper;
  private final BulkProcessor bulkProcessor;

  public IndexationEngineImpl(Class<DTO> supportedClass, Client client, Resource source, String alias, Function<DTO, String> typeFunction, ReadOnlyService<DTO> service,
                              BulkProcessor bulkProcessor, ObjectMapper objectMapper) {
    this.supportedClass = supportedClass;
    this.client = client;
    this.source = source;
    this.alias = alias;
    this.typeFunction = typeFunction;
    this.service = service;
    this.bulkProcessor = bulkProcessor;
    this.objectMapper = objectMapper;
  }

  @Override
  public void indexFull() {
    String newIndexName = this.createIndex();
    try {
      Pageable pageable = new PageRequest(0, 1000);
      Page<DTO> pagedDTOs = null;
      do {
        pagedDTOs = this.service.getAll(pageable);
        List<DTO> dtos = pagedDTOs.getContent();

        for (DTO dto : dtos) {
          this.bulkProcessor.add(this.prepareIndexRequest(newIndexName, dto).request());
        }

        pageable = pagedDTOs.nextPageable();

      } while (pagedDTOs.hasNext());

      this.bulkProcessor.flush();
      this.switchIndex(newIndexName);

      logger.info("Full indexing of {} elements ended.", this.alias);

    } catch (Exception e) {
      this.client.admin().indices().prepareDelete(newIndexName).get();
      logger.error("Can't index {} elements", this.alias, e);
    }
  }


  @Override
  public void indexOne(long id) {
    if(!existsIndex()){
      logger.debug("Can't delete {} element with id {} as the index doesn't exist !", this.alias, id);
      return;
    }

    try {
      DTO dto = this.service.getOne(id);
      if (dto != null) {
        this.prepareIndexRequest(this.alias, dto).get();
      }
    } catch (Exception e) {
      logger.error("Can't index {} element with id {}", this.alias, id, e);
    }
  }

  @Override
  public void updateOne(long id) {
    this.indexOne(id);
  }

  @Override
  public void deleteOne(long id) {
    if(!existsIndex()){
      logger.debug("Can't delete {} element with id {} as the index doesn't exist !", this.alias, id);
      return;
    }
    try {
      this.prepareDeleteRequest(this.alias, this.service.getOne(id)).get();
    } catch (Exception e) {
      logger.error("Can't delete {} element with id {}", this.alias, id, e);
    }
  }

  private boolean existsIndex(){
    return !getIndicesFromAliasName().isEmpty();
  }

  private String createIndex() {
    final String indexName = this.alias + "_" + System.currentTimeMillis();
    CreateIndexRequestBuilder prepareCreate = this.client.admin().indices().prepareCreate(indexName);

    if (this.source != null) {
      try {
        prepareCreate.setSource(ByteStreams.toByteArray(this.source.getInputStream()));
      } catch (IOException e) {
        logger.error("Can't read index {} configuration file {}", indexName, this.source, e);
      }
    }

    CreateIndexResponse createIndexResponse = prepareCreate.get();
    if (!createIndexResponse.isAcknowledged()) {
      throw new IllegalStateException("Error creating index for Lotus BO");
    }
    return indexName;
  }

  private void switchIndex(String newIndexName) {
    IndicesAliasesRequestBuilder aliasBuilder = this.client.admin().indices().prepareAliases();
    Set<String> indicesNamesForAlias = this.getIndicesFromAliasName();

    if (indicesNamesForAlias != null && !indicesNamesForAlias.isEmpty()) {
      for (String oldIndexName : indicesNamesForAlias) {
        aliasBuilder.removeAlias(oldIndexName, this.alias);
      }
    }

    aliasBuilder.addAlias(newIndexName, this.alias);
    aliasBuilder.get();

    if (indicesNamesForAlias != null && !indicesNamesForAlias.isEmpty()) {
      this.client.admin().indices().prepareDelete(indicesNamesForAlias.toArray(new String[indicesNamesForAlias.size()])).get();
    }
  }

  Set<String> getIndicesFromAliasName() {
    IndicesAdminClient iac = this.client.admin().indices();
    ImmutableOpenMap<String, List<AliasMetaData>> map = iac.getAliases(new GetAliasesRequest(this.alias)).actionGet().getAliases();

    final Set<String> allIndices = Sets.newHashSet();
    map.keysIt().forEachRemaining(allIndices::add);
    return allIndices;
  }

  private UpdateRequestBuilder prepareIndexRequest(String indexName, DTO dto) throws JsonProcessingException {
    ObjectNode json = this.objectMapper.valueToTree(dto);
    return this.client.prepareUpdate(indexName, this.typeFunction.apply(dto), String.valueOf(dto.getId())).setDocAsUpsert(true).setDoc(this.objectMapper.writeValueAsString(json));
  }

  private DeleteRequestBuilder prepareDeleteRequest(String indexName, DTO dto) {
    return this.client.prepareDelete().setIndex(indexName).setType(this.typeFunction.apply(dto)).setId(String.valueOf(dto.getId()));
  }

  @Override
  public boolean supports(Class<? extends AbstractDTO> delimiter) {
    return delimiter.isAssignableFrom(this.supportedClass);
  }

}
