package fr.mgargadennec.blossom.core.common.actuator;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.common.unit.TimeValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.InMemoryTraceRepository;

import java.util.Map;

/**
 * Created by maelg on 16/05/2017.
 */

public class ElasticsearchTraceRepositoryImpl extends InMemoryTraceRepository implements ElasticsearchTraceRepository {
    private final static Logger logger = LoggerFactory.getLogger(ElasticsearchTraceRepository.class);
    private final Client client;
    private final String index;

    public ElasticsearchTraceRepositoryImpl(Client client, String index) {
        this.client = client;
        this.index = index;
        if(!this.client.admin().indices().prepareExists(index).get().isExists()) {
            this.client.admin().indices().prepareCreate(index).addMapping("traces", "{\"traces\" : {\"_ttl\" : {\"enabled\": true,  \"default\" : \"7d\" } }" +
                    "}").get();
        }
    }

    @Override
    public void add(Map<String, Object> traceInfo) {
        super.add(traceInfo);
        client.prepareIndex(index, index).setSource(traceInfo).execute(new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                logger.trace("Indexed trace into elasticsearch", traceInfo);
            }

            @Override
            public void onFailure(Throwable throwable) {
            }
        });
    }

}
