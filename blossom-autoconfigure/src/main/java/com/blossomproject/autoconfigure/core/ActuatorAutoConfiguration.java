package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.common.actuator.ElasticsearchTraceRepository;
import com.blossomproject.core.common.actuator.ElasticsearchTraceRepositoryImpl;
import com.blossomproject.core.common.actuator.TraceStatisticsMvcEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import java.io.IOException;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
@AutoConfigureAfter(ElasticsearchAutoConfiguration.class)
@AutoConfigureBefore(HttpTraceAutoConfiguration.class)
@PropertySource("classpath:/actuator.properties")
public class ActuatorAutoConfiguration {

  @Bean
  public ElasticsearchTraceRepository traceRepository(Client client, BulkProcessor bulkProcessor,
    @Value("classpath:/elasticsearch/traces.json") Resource resource, ObjectMapper objectMapper)
    throws IOException {
    String settings = Resources.toString(resource.getURL(), Charsets.UTF_8);

    return new ElasticsearchTraceRepositoryImpl(client, bulkProcessor,"traces", Lists
      .newArrayList("/blossom.*", "/favicon.*", "/js.*", "/css.*", "/fonts.*", "/img.*",
        "/font-awesome.*"), settings, objectMapper);
  }

  @Bean
  public TraceStatisticsMvcEndpoint traceStatisticsMvcEndpoint(
    ElasticsearchTraceRepository traceRepository) {
    return new TraceStatisticsMvcEndpoint(traceRepository);
  }
}
