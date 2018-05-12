package com.blossomproject.autoconfigure.core;

import com.blossomproject.core.common.actuator.ElasticsearchTraceRepository;
import com.blossomproject.core.common.actuator.ElasticsearchTraceRepositoryImpl;
import com.blossomproject.core.common.actuator.TraceStatisticsMvcEndpoint;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.trace.http.HttpTraceAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
@AutoConfigureAfter(ElasticsearchAutoConfiguration.class)
@AutoConfigureBefore(HttpTraceAutoConfiguration.class)
@PropertySource("classpath:/actuator.properties")
public class ActuatorAutoConfiguration {

  @Configuration("BlossomActuatorAutoConfigurationTraceProperties")
  @ConfigurationProperties("blossom.actuator.traces")
  @PropertySource("classpath:/actuator.properties")
  public static class TraceProperties {
    private final Set<String> excludedUris = new HashSet<>();
    private final Set<String> excludedRequestHeaders = new HashSet<>();
    private final Set<String> excludedResponseHeaders = new HashSet<>();

    public Set<String> getExcludedUris() {
      return excludedUris;
    }

    public Set<String> getExcludedRequestHeaders() {
      return excludedRequestHeaders;
    }

    public Set<String> getExcludedResponseHeaders() {
      return excludedResponseHeaders;
    }
  }

  @Bean
  public ElasticsearchTraceRepository traceRepository(
    Client client, BulkProcessor bulkProcessor,
    @Value("classpath:/elasticsearch/traces.json") Resource resource,
    ObjectMapper objectMapper,
    TraceProperties traceProperties)
    throws IOException {
    String settings = Resources.toString(resource.getURL(), Charsets.UTF_8);

    return new ElasticsearchTraceRepositoryImpl(
      client, bulkProcessor, "traces", traceProperties.getExcludedUris(),
      traceProperties.getExcludedRequestHeaders(), traceProperties.getExcludedResponseHeaders(), settings, objectMapper);
  }

  @Bean
  public TraceStatisticsMvcEndpoint traceStatisticsMvcEndpoint(
    ElasticsearchTraceRepository traceRepository) {
    return new TraceStatisticsMvcEndpoint(traceRepository);
  }
}
