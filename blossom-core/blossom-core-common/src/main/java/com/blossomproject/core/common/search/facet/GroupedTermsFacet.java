package com.blossomproject.core.common.search.facet;

import java.util.List;

public class GroupedTermsFacet extends Facet{

  public GroupedTermsFacet(String name, String path){
    super(FacetType.GROUPED_TERMS, name, path);
  }

  private List<GroupedTermsFacetResult> results;

  public List<GroupedTermsFacetResult> getResults() {
    return results;
  }

  public void setResults(List<GroupedTermsFacetResult> results) {
    this.results = results;
  }

  public static class GroupedTermsFacetResult<T>{
    private String group;
    private Long count;
    private List<T> terms;

    public String getGroup() {
      return group;
    }

    public void setGroup(String group) {
      this.group = group;
    }

    public Long getCount() {
      return count;
    }

    public void setCount(Long count) {
      this.count = count;
    }

    public List<T> getTerms() {
      return terms;
    }

    public void setTerms(List<T> terms) {
      this.terms = terms;
    }
  }
}
