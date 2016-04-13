package fr.mgargadennec.blossom.core.indexing.support.indexation.builder.impl;

import java.util.Map;

public class BlossomSearchFilter {
  private String queryString = null;

  private Map<String, Object> filters = null;

  public String getQueryString() {
    return queryString;
  }

  public void setQueryString(String queryString) {
    this.queryString = queryString;
  }

  public Map<String, Object> getFilters() {
    return filters;
  }

  public void setFilters(Map<String, Object> filters) {
    this.filters = filters;
  }

}
