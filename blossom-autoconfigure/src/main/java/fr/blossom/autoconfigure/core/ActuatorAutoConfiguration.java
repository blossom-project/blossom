package fr.blossom.autoconfigure.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import fr.blossom.core.common.actuator.ElasticsearchTraceRepository;
import fr.blossom.core.common.actuator.ElasticsearchTraceRepositoryImpl;
import fr.blossom.core.common.actuator.TraceStatisticsMvcEndpoint;
import io.micrometer.core.instrument.binder.jvm.ClassLoaderMetrics;
import java.io.IOException;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.web.trace.HttpTraceAutoConfiguration;
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
  public ElasticsearchTraceRepository traceRepository(Client client,
    @Value("classpath:/elasticsearch/traces.json") Resource resource, ObjectMapper objectMapper)
    throws IOException {
    String settings = Resources.toString(resource.getURL(), Charsets.UTF_8);

    return new ElasticsearchTraceRepositoryImpl(client, "traces", Lists
      .newArrayList("/blossom.*", "/favicon.*", "/js.*", "/css.*", "/fonts.*", "/img.*",
        "/font-awesome.*"), settings, objectMapper);
  }

  @Bean
  public ClassLoaderMetrics classLoaderMetrics(){
    return new ClassLoaderMetrics();
  }


  @Bean
  public TraceStatisticsMvcEndpoint traceStatisticsMvcEndpoint(
    ElasticsearchTraceRepository traceRepository) {
    return new TraceStatisticsMvcEndpoint(traceRepository);
  }
}
