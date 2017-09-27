package fr.mgargadennec.blossom.autoconfigure.core;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import fr.mgargadennec.blossom.core.common.actuator.ElasticsearchTraceRepository;
import fr.mgargadennec.blossom.core.common.actuator.ElasticsearchTraceRepositoryImpl;
import fr.mgargadennec.blossom.core.common.actuator.TraceStatisticsEndpoint;
import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import fr.mgargadennec.blossom.core.common.utils.privilege.SimplePrivilege;
import java.io.IOException;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.TraceRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
@AutoConfigureBefore(TraceRepositoryAutoConfiguration.class)
@AutoConfigureAfter(ElasticsearchAutoConfiguration.class)
public class ActuatorAutoConfiguration {


  private Resource traceResource;

  @Bean
  @ConditionalOnMissingBean(ElasticsearchTraceRepository.class)
  public ElasticsearchTraceRepository traceRepository(Client client,
    @Value("classpath:/elasticsearch/traces.json") Resource resource) throws IOException {
    String settings = Resources.toString(resource.getURL(), Charsets.UTF_8);

    return new ElasticsearchTraceRepositoryImpl(client, "traces", Lists
      .newArrayList("/blossom.*", "/favicon.*", "/js.*", "/css.*", "/fonts.*", "/img.*",
        "/font-awesome.*"), settings);
  }

  @Bean
  @ConditionalOnBean(ElasticsearchTraceRepository.class)
  public TraceStatisticsEndpoint traceStatisticsEndpoint(
    ElasticsearchTraceRepository elasticsearchTraceRepository) {
    return new TraceStatisticsEndpoint(elasticsearchTraceRepository);
  }
}
