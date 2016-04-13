package fr.mgargadennec.blossom.core.indexing.configuration;

import org.elasticsearch.client.Client;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.BlossomElasticsearchQueryServiceImpl;
import fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl.IBlossomElasticsearchQueryService;
import fr.mgargadennec.blossom.security.core.support.security.IBlossomAuthenticationUtilService;

@Configuration
public class BlossomIndexingConfiguration {

	@Bean
	public IBlossomElasticsearchQueryService blossomElasticsearchQueryService(Client esClient,IBlossomAuthenticationUtilService blossomAuthenticationUtilService ){
		return new BlossomElasticsearchQueryServiceImpl(esClient, blossomAuthenticationUtilService);
	}
}
