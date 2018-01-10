package fr.blossom.autoconfigure.core;

import java.nio.file.Files;
import java.nio.file.Path;
import org.elasticsearch.client.Client;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.util.concurrent.EsExecutors;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnClass({Client.class})
@EnableConfigurationProperties({ElasticsearchProperties.class})
public class ElasticsearchAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public Client elasticsearchClient() throws Exception {
    Path homePath = Files.createTempDirectory("esHome");

    return NodeBuilder.nodeBuilder()
      .local(true)
      .data(true)
      .settings(Settings
        .builder()
        .put(IndexMetaData.SETTING_NUMBER_OF_SHARDS, 1)
        .put(IndexMetaData.SETTING_NUMBER_OF_REPLICAS, 0)
        .put(EsExecutors.PROCESSORS, 1)
        .put("path.home", homePath.toFile().getAbsolutePath())
        .build())
      .node()
      .client();
  }

}
