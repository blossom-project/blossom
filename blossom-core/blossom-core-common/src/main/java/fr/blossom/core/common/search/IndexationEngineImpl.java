package fr.blossom.core.common.search;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.Sets;
import com.google.common.io.ByteStreams;
import fr.blossom.core.common.dto.AbstractDTO;
import fr.blossom.core.common.service.ReadOnlyService;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.elasticsearch.action.admin.indices.alias.IndicesAliasesRequestBuilder;
import org.elasticsearch.action.admin.indices.alias.get.GetAliasesRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexRequestBuilder;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.action.admin.indices.delete.DeleteIndexRequest;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.delete.DeleteRequestBuilder;
import org.elasticsearch.action.update.UpdateRequestBuilder;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.IndicesAdminClient;
import org.elasticsearch.cluster.metadata.AliasMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class IndexationEngineImpl<DTO extends AbstractDTO> implements IndexationEngine {

  private final static Logger logger = LoggerFactory.getLogger(IndexationEngineImpl.class);
  private final Client client;
  private final ReadOnlyService<DTO> service;
  private final IndexationEngineConfiguration<DTO> configuration;

  private final ObjectMapper objectMapper;
  private final BulkProcessor bulkProcessor;

  public IndexationEngineImpl(Client client, ReadOnlyService<DTO> service, BulkProcessor bulkProcessor, ObjectMapper objectMapper, IndexationEngineConfiguration<DTO> configuration) {
    this.client = client;
    this.service = service;
    this.bulkProcessor = bulkProcessor;
    this.objectMapper = objectMapper;
    this.configuration = configuration;
  }

  @Override
  public void indexFull() {
    this.cleanOrphanIndex();
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

      logger.info("Full indexing of {} {} ended.", pagedDTOs.getTotalElements(), this.configuration.getAlias());

    } catch (Exception e) {
      this.client.admin().indices().prepareDelete(newIndexName).get();
      logger.error("Can't index {} elements", this.configuration.getAlias(), e);
    }
  }

  @Override
  public void indexOne(long id) {
    if (!existsIndex()) {
      logger
        .debug("Can't delete {} element with id {} as the index doesn't exist !", this.configuration.getAlias(), id);
      return;
    }

    try {
      DTO dto = this.service.getOne(id);
      if (dto != null) {
        this.prepareIndexRequest(this.configuration.getAlias(), dto).get();
      }
    } catch (Exception e) {
      logger.error("Can't index {} element with id {}", this.configuration.getAlias(), id, e);
    }
  }

  @Override
  public void updateOne(long id) {
    this.indexOne(id);
  }

  @Override
  public void deleteOne(long id) {
    if (!existsIndex()) {
      logger
        .debug("Can't delete {} element with id {} as the index doesn't exist !", this.configuration.getAlias(), id);
      return;
    }
    try {
      DTO dto = this.service.getOne(id);
      if (dto != null) {
        this.prepareDeleteRequest(this.configuration.getAlias(), dto).get();
      }
    } catch (Exception e) {
      logger.error("Can't delete {} element with id {}", this.configuration.getAlias(), id, e);
    }
  }

  private void cleanOrphanIndex() {
    String[] indices = client.admin().cluster().prepareState().execute().actionGet().getState()
      .getMetaData().getConcreteAllIndices();

    List<String> orphans = Stream.of(indices)
      .filter(i -> i.startsWith(this.configuration.getAlias()))
      .filter(i -> 0 == client.admin().indices().getAliases(new GetAliasesRequest().indices(i))
        .actionGet().getAliases().size())
      .collect(Collectors.toList());

    for (String orphan : orphans) {
      client.admin().indices().delete(new DeleteIndexRequest(orphan)).actionGet();
    }
  }

  private boolean existsIndex() {
    return !getIndicesFromAliasName().isEmpty();
  }

  private String createIndex() {
    final String indexName = this.configuration.getAlias() + "_" + System.currentTimeMillis();
    CreateIndexRequestBuilder prepareCreate = this.client.admin().indices()
      .prepareCreate(indexName);

    if (this.configuration.getSource() != null) {
      try {
        prepareCreate.setSource(ByteStreams.toByteArray(this.configuration.getSource().getInputStream()));
      } catch (IOException e) {
        logger.error("Can't read index {} configuration file {}", indexName, this.configuration.getSource(), e);
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
        aliasBuilder.removeAlias(oldIndexName, this.configuration.getAlias());
      }
    }

    aliasBuilder.addAlias(newIndexName, this.configuration.getAlias());
    aliasBuilder.get();

    if (indicesNamesForAlias != null && !indicesNamesForAlias.isEmpty()) {
      this.client.admin().indices()
        .prepareDelete(indicesNamesForAlias.toArray(new String[indicesNamesForAlias.size()])).get();
    }
  }

  Set<String> getIndicesFromAliasName() {
    IndicesAdminClient iac = this.client.admin().indices();
    ImmutableOpenMap<String, List<AliasMetaData>> map = iac
      .getAliases(new GetAliasesRequest(this.configuration.getAlias())).actionGet().getAliases();

    final Set<String> allIndices = Sets.newHashSet();
    map.keysIt().forEachRemaining(allIndices::add);
    return allIndices;
  }

  private UpdateRequestBuilder prepareIndexRequest(String indexName, DTO dto)
    throws JsonProcessingException {
    ObjectNode json = prepareDocument(dto);
    return this.client
      .prepareUpdate(indexName, this.configuration.getTypeFunction().apply(dto), String.valueOf(dto.getId()))
      .setDocAsUpsert(true).setDoc(this.objectMapper.writeValueAsString(json));
  }

  protected ObjectNode prepareDocument(DTO dto) {
    ObjectNode root = objectMapper.createObjectNode();
    root.putPOJO("summary", this.configuration.getSummaryFunction().apply(dto));
    root.set("dto", this.objectMapper.valueToTree(dto));
    return root;
  }

  private DeleteRequestBuilder prepareDeleteRequest(String indexName, DTO dto) {
    return this.client.prepareDelete().setIndex(indexName).setType(this.configuration.getTypeFunction().apply(dto))
      .setId(String.valueOf(dto.getId()));
  }

  @Override
  public boolean supports(Class<? extends AbstractDTO> delimiter) {
    return delimiter.isAssignableFrom(this.configuration.getSupportedClass());
  }

}
