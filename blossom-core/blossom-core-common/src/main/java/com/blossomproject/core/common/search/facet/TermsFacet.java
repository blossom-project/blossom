package com.blossomproject.core.common.search.facet;

import java.util.List;

public class TermsFacet extends Facet{

  public TermsFacet(String name, String path){
    super(FacetType.TERMS, name, path);
  }

  private List<TermsFacetResult> results;

  public List<TermsFacetResult> getResults() {
    return results;
  }

  public void setResults(List<TermsFacetResult> results) {
    this.results = results;
  }

  public static class TermsFacetResult{
    private String term;
    private String value;
    private Long count;

    public String getTerm() {
      return term;
    }

    public void setTerm(String term) {
      this.term = term;
    }

    public String getValue() {
      return value;
    }

    public void setValue(String value) {
      this.value = value;
    }

    public Long getCount() {
      return count;
    }

    public void setCount(Long count) {
      this.count = count;
    }
  }
}
