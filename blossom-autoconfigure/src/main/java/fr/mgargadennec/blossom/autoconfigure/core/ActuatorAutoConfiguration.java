package fr.mgargadennec.blossom.autoconfigure.core;

import fr.mgargadennec.blossom.core.common.actuator.ElasticsearchTraceRepository;
import fr.mgargadennec.blossom.core.common.actuator.ElasticsearchTraceRepositoryImpl;
import org.elasticsearch.client.Client;
import org.springframework.boot.actuate.autoconfigure.TraceRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
@AutoConfigureBefore(TraceRepositoryAutoConfiguration.class)
@AutoConfigureAfter(ElasticsearchAutoConfiguration.class)
public class ActuatorAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean(ElasticsearchTraceRepository.class)
    public ElasticsearchTraceRepository traceRepository(Client client){
        return new ElasticsearchTraceRepositoryImpl(client,"traces");
    }
}
