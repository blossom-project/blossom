package fr.mgargadennec.blossom.core.common.configuration.root.technical;

import org.elasticsearch.client.Client;
import org.elasticsearch.common.settings.ImmutableSettings;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.node.Node;
import org.elasticsearch.node.NodeBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BlossomCommonElasticsearchConfiguration {
	 @Bean
	  public Settings settings() {
	    return ImmutableSettings.settingsBuilder().put("script.disable_dynamic", false).build();
	  }

	  @Bean(destroyMethod = "close")
	  public Node esNode(Settings settings) {
	    return NodeBuilder.nodeBuilder().settings(settings).local(true).node();
	  }

	  @Bean(destroyMethod = "close")
	  public Client esClient(Node node) {
	    return node.client();
	  }
	
}
