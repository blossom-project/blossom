package com.blossomproject.core.common.search.facet;

import com.blossomproject.core.common.search.SearchEngine;
import com.google.common.base.Preconditions;

public abstract class AggregationConverterAbstractImpl implements AggregationConverter {
  private final String name;
  private final String supportedSearchEngine;

  public AggregationConverterAbstractImpl(String name, String supportedSearchEngine) {
    Preconditions.checkNotNull(name);
    Preconditions.checkNotNull(supportedSearchEngine);
    this.name = name;
    this.supportedSearchEngine = supportedSearchEngine;
  }

  @Override
  public String name() {
    return this.name;
  }

  @Override
  public String supportedSearchEngine() {
    return this.supportedSearchEngine;
  }

  @Override
  public boolean supports(SearchEngine delimiter) {
    return delimiter.getName().equals(this.supportedSearchEngine);
  }
}
