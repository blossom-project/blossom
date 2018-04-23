package com.blossomproject.core.common.search.facet;

import com.blossomproject.core.common.search.facet.TermsFacet.TermsFacetResult;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;

public class AggregationConverterTermImpl extends AggregationConverterAbstractImpl {

  private final String field;

  public AggregationConverterTermImpl(String name, String supportedSearchEngine, String field) {
    super(name, supportedSearchEngine);
    this.field = field;
  }

  @Override
  public List<AggregationBuilder> encode(FacetConfiguration configuration) {
    String name = this.name();
    TermsFacetConfiguration config = (TermsFacetConfiguration) configuration;
    config.setUuid(UUID.randomUUID().toString());


    AggregationBuilder aggregation = AggregationBuilders
      .terms(name)
      .field(field)
      .size(config.getSize());

    return Lists.newArrayList(aggregation);
  }

  @Override
  public Facet decode(Supplier<Aggregations> parent, FacetConfiguration configuration) {
    String name = this.name();
    StringTerms aggregation = parent.get().get(name);

    TermsFacet facet = new TermsFacet(this.name(), field);
    facet.setMultiple(true);
    facet.setResults(aggregation.getBuckets().stream().map(b -> {
      TermsFacetResult result = new TermsFacetResult();
      result.setTerm(b.getKeyAsString());
      result.setValue(b.getKeyAsString());
      result.setCount(b.getDocCount());
      return result;
    }).collect(Collectors.toList()));

    return facet;
  }
}
