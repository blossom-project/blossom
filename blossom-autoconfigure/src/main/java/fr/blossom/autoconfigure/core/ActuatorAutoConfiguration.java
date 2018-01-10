package fr.blossom.autoconfigure.core;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;
import fr.blossom.core.common.actuator.ElasticsearchTraceRepository;
import fr.blossom.core.common.actuator.ElasticsearchTraceRepositoryImpl;
import fr.blossom.core.common.actuator.TraceStatisticsMvcEndpoint;
import org.elasticsearch.client.Client;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.actuate.autoconfigure.trace.TraceRepositoryAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.data.elasticsearch.ElasticsearchAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
@AutoConfigureAfter(ElasticsearchAutoConfiguration.class)
@AutoConfigureBefore(TraceRepositoryAutoConfiguration.class)
public class ActuatorAutoConfiguration {

    @Bean
    public ElasticsearchTraceRepository traceRepository(Client client,
                                                        @Value("classpath:/elasticsearch/traces.json") Resource resource) throws IOException {
        String settings = Resources.toString(resource.getURL(), Charsets.UTF_8);

        return new ElasticsearchTraceRepositoryImpl(client, "traces", Lists
                .newArrayList("/blossom.*", "/favicon.*", "/js.*", "/css.*", "/fonts.*", "/img.*",
                        "/font-awesome.*"), settings);
    }


    @Bean
    public TraceStatisticsMvcEndpoint traceStatisticsMvcEndpoint(ElasticsearchTraceRepository traceRepository) {
        return new TraceStatisticsMvcEndpoint(traceRepository);
    }
}
