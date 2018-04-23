package com.blossomproject.core.common.search.facet;

import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.core.common.search.SearchEngine;
import java.util.List;
import java.util.function.Supplier;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.Aggregations;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;

@Qualifier(PluginConstants.PLUGIN_SEARCH_ENGINE_AGGREGATION_CONVERTERS)
public interface AggregationConverter extends Plugin<SearchEngine> {

  String name();

  List<AggregationBuilder> encode(FacetConfiguration configuration);

  Facet decode(Supplier<Aggregations> parent, FacetConfiguration configuration);

}
