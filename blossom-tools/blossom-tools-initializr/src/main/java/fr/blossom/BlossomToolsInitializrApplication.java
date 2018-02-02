package fr.blossom;

import fr.blossom.initializr.Initializr;
import fr.blossom.initializr.ProjectGenerator;
import java.net.InetAddress;
import java.net.UnknownHostException;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkProcessor.Listener;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

@SpringBootApplication
public class BlossomToolsInitializrApplication {

  public static void main(String[] args) {
    SpringApplication.run(BlossomToolsInitializrApplication.class, args);
  }

  @Bean
  public Initializr initializr() {
    return new Initializr();
  }

  @Bean
  public ProjectGenerator projectGenerator(Initializr initializr, ResourceLoader resourceLoader) {
    return new ProjectGenerator(initializr, resourceLoader);
  }

  @Configuration
  public class ElasticsearchConfiguration {

    @Value("${elasticsearch.host:127.0.0.1}")
    private String host;
    @Value("${elasticsearch.port:9300}")
    private Integer port;
    @Value("${elasticsearch.cluster.name:elasticsearch}")
    private String clustername;

    @Bean
    public Settings esSettings() {
      return Settings.builder().put("cluster.name", clustername).build();
    }

    @Bean(destroyMethod = "close")
    public Client esClient() throws UnknownHostException {
      PreBuiltTransportClient client = new PreBuiltTransportClient(esSettings());
      client.addTransportAddress(new TransportAddress(InetAddress.getByName(host), 9300));
      client.connectedNodes();
      return client;
    }

    @Bean
    public BulkProcessor bulkProcessor() throws UnknownHostException {
      return BulkProcessor.builder(esClient(), new Listener() {
        @Override
        public void beforeBulk(long l, BulkRequest bulkRequest) {
        }

        @Override
        public void afterBulk(long l, BulkRequest bulkRequest, BulkResponse bulkResponse) {
        }

        @Override
        public void afterBulk(long l, BulkRequest bulkRequest, Throwable throwable) {
        }
      }).setFlushInterval(TimeValue.timeValueSeconds(5)).build();
    }

    @Bean
    public CommandLineRunner initIndex(final Client client){
      return args ->{
        if (!client.admin().indices().prepareExists("generations").get().isExists()) {
          client.admin().indices().prepareCreate("generations").get();
        }
      };
    }
  }

}
