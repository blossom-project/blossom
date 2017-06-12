package fr.mgargadennec.blossom.core.common.actuator;

import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.RangeQueryBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.trace.InMemoryTraceRepository;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by maelg on 16/05/2017.
 */

public class ElasticsearchTraceRepositoryImpl extends InMemoryTraceRepository implements ElasticsearchTraceRepository {
    private final static Logger logger = LoggerFactory.getLogger(ElasticsearchTraceRepository.class);
    private final Client client;
    private final String index;
    private final List<Pattern> ignoredPatterns;
    private final String settings;

    public ElasticsearchTraceRepositoryImpl(Client client, String index, List<String> ignoredUris, String settings) {
        this.client = client;
        this.index = index;
        this.ignoredPatterns = ignoredUris.stream().map(pattern -> Pattern.compile(pattern)).collect(Collectors.toList());
        this.settings = settings;

        if (!this.client.admin().indices().prepareExists(index).get().isExists()) {
            this.client.admin().indices().prepareCreate(index).setSource(settings).get();
        }
    }

    @Override
    public void add(Map<String, Object> traceInfo) {
        String path = (String) traceInfo.get("path");
        boolean ignore = ignoredPatterns.stream().anyMatch(pattern -> pattern.matcher(path).matches());
        if (!ignore) {

            traceInfo.put("timestamp", Instant.now().toEpochMilli());
            super.add(traceInfo);

            this.client.prepareIndex(index, index).setSource(traceInfo).execute(new ActionListener<IndexResponse>() {
                @Override
                public void onResponse(IndexResponse indexResponse) {
                    logger.trace("Indexed trace into elasticsearch {}", traceInfo);
                }

                @Override
                public void onFailure(Throwable throwable) {
                    logger.error("Indexed trace into elasticsearch {} {}", traceInfo, throwable);
                }
            });
        }
    }

    @Override
    public SearchResponse stats(Instant from, Instant to, String precision) {
        if (from == null) {
            from = LocalDate.now().minusDays(7L).atStartOfDay().toInstant(ZoneOffset.UTC);
        }
        if (to == null) {
            to = LocalDate.now().plusDays(1L).atStartOfDay().toInstant(ZoneOffset.UTC);
        }
        if(precision == null){
          precision = "2h";
        }

        RangeQueryBuilder dateFilter = QueryBuilders.rangeQuery("timestamp").includeLower(true).includeUpper(true);
        dateFilter.from(from.toEpochMilli());
        dateFilter.to(to.toEpochMilli());

        SearchRequestBuilder request = this.client.prepareSearch(this.index)
                .setQuery(QueryBuilders.boolQuery().filter(dateFilter))
                .setSize(0)
                .addAggregation(AggregationBuilders.terms("methods").field("method"))
                .addAggregation(AggregationBuilders.histogram("response_time_histogram").field("timeTaken").interval(100))
                .addAggregation(AggregationBuilders.extendedStats("response_time_stats").field("timeTaken"))
                .addAggregation(AggregationBuilders.terms("response_status_stats").field("headers.response.status"))
                .addAggregation(AggregationBuilders.terms("response_content_type_stats").field("headers.response.Content-Type"))
                .addAggregation(AggregationBuilders.terms("top_uris").field("path").order(Terms.Order.aggregation("_count",false)).size(10))
                .addAggregation(AggregationBuilders.terms("flop_uris").field("path").order(Terms.Order.aggregation("_count",true)).size(10))
                .addAggregation(AggregationBuilders.dateHistogram("request_histogram").field("timestamp").interval(new DateHistogramInterval(precision)).subAggregation(AggregationBuilders.terms("methods").field("method")));
        SearchResponse response = request.execute().actionGet();

        return response;
    }

}
