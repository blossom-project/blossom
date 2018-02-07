package com.blossom_project.core.common.search;


import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.Maps;
import com.blossom_project.core.common.dto.AbstractDTO;
import com.blossom_project.core.common.service.ReadOnlyService;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

;

@RunWith(MockitoJUnitRunner.class)
public class IndexationEngineImplTest {

  private Client client;
  private ReadOnlyService<TestDTO> service;
  private BulkProcessor bulkProcessor;
  private ObjectMapper objectMapper;
  private IndexationEngineConfiguration configuration;
  private IndexationEngineImpl engine;

  @Before
  public void setUp() throws Exception {
    this.client = mock(Client.class);
    this.service = mock(ReadOnlyService.class);
    this.bulkProcessor = mock(BulkProcessor.class);
    this.objectMapper = spy(new ObjectMapper());
    this.configuration = mock(IndexationEngineConfiguration.class);
    this.engine = spy(new IndexationEngineImpl(this.client, this.service, this.bulkProcessor, this.objectMapper, this.configuration));
  }

  private final static SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");

  @Test
  public void should_object_writer_ignore_configuration_of_object_mapper()
    throws ParseException, JsonProcessingException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.setDateFormat(df);

    String toParse = "20-12-2014 02:30:00";
    Date date = df.parse(toParse);
    Map<String, Object> event = Maps.newHashMap();
    event.put("party", date);

    String result = mapper.writeValueAsString(event);
    assertThat(result, containsString(toParse));

    ObjectWriter writer = mapper.writer(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    String writerResult = writer.writeValueAsString(event);
    assertThat(writerResult, not(containsString(toParse)));
  }

  @Test
  @Ignore
  public void should_index_one() throws Exception {
    Long id = 1L;
    when(this.service.getOne(eq(id))).thenReturn(new TestDTO());

    this.engine.indexOne(id);
  }

  private class TestDTO extends AbstractDTO {

  }
}
