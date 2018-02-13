package com.blossomproject.autoconfigure.core;

import com.google.common.base.Splitter;
import com.blossomproject.autoconfigure.core.elasticsearch.ElasticsearchProperties;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.lease.Releasable;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.util.StringUtils;

@Configuration
@PropertySource("classpath:/elasticsearch.properties")
@ConditionalOnClass({Client.class})
@EnableConfigurationProperties(ElasticsearchProperties.class)
public class ElasticsearchAutoConfiguration implements DisposableBean {

  private static final Logger logger = LoggerFactory
    .getLogger(ElasticsearchAutoConfiguration.class);
  private static final Map<String, String> DEFAULTS;

  static {
    Map<String, String> defaults = new LinkedHashMap<String, String>();
    defaults.put("http.enabled", String.valueOf(true));
    defaults.put("node.local", String.valueOf(true));
    defaults.put("path.home", System.getProperty("user.dir"));
    DEFAULTS = Collections.unmodifiableMap(defaults);
  }

  private final ElasticsearchProperties properties;
  private Releasable releasable;

  public ElasticsearchAutoConfiguration(ElasticsearchProperties properties) {
    this.properties = properties;
  }

  @Bean
  @ConditionalOnMissingBean
  public Client elasticsearchClient() {
    try {
      return createClient();
    } catch (Exception ex) {
      throw new IllegalStateException(ex);
    }
  }

  private Client createClient() throws Exception {
    if (StringUtils.hasLength(this.properties.getClusterNodes())) {
      return createTransportClient();
    }
    return createNodeClient();
  }

  private Client createNodeClient() throws Exception {
    Settings.Builder settings = Settings.settingsBuilder();
    for (Map.Entry<String, String> entry : DEFAULTS.entrySet()) {
      if (!this.properties.getProperties().containsKey(entry.getKey())) {
        settings.put(entry.getKey(), entry.getValue());
      }
    }
    settings.put(this.properties.getProperties());
    Node node = new NodeBuilder().settings(settings).clusterName(this.properties.getClusterName())
      .node();
    this.releasable = node;
    return node.client();
  }

  private Client createTransportClient() throws Exception {
    TransportClient.Builder builder = new TransportClient.Builder();
    builder.settings(Settings.settingsBuilder().put(createProperties()));
    TransportClient client = builder.build();

    Splitter.on(",").splitToList(this.properties.getClusterNodes()).forEach(
      a -> {
        String[] hostAndPort = a.split(":");
        try {
          client.addTransportAddress(new InetSocketTransportAddress(
            InetAddress.getByName(hostAndPort[0]),
            Integer.parseInt(hostAndPort[1])));
        } catch (UnknownHostException e) {
          throw new RuntimeException("Cannot connect to inet address " + hostAndPort[0], e);
        }
      }
    );
    this.releasable = client;
    return client;
  }

  private Properties createProperties() {
    Properties properties = new Properties();
    properties.put("cluster.name", this.properties.getClusterName());
    properties.putAll(this.properties.getProperties());
    return properties;
  }

  @Override
  public void destroy() throws Exception {
    if (this.releasable != null) {
      try {
        if (logger.isInfoEnabled()) {
          logger.info("Closing Elasticsearch client");
        }
        this.releasable.close();
      } catch (final Exception ex) {
        if (logger.isErrorEnabled()) {
          logger.error("Error closing Elasticsearch client: ", ex);
        }
      }
    }
  }

}
