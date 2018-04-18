package com.blossomproject.core.common.search.facet;

import java.util.List;

public class RangesFacet extends Facet{

  public RangesFacet(String name, String path){
    super(FacetType.RANGES, name, path);
  }

  private List<RangesFacetResult> results;

  public List<RangesFacetResult> getResults() {
    return results;
  }

  public void setResults(List<RangesFacetResult> results) {
    this.results = results;
  }

  public static class RangesFacetResult{
    private String term;
    private Double from;
    private Double to;
    private Long count;

    public String getTerm() {
      return term;
    }

    public void setTerm(String term) {
      this.term = term;
    }

    public Long getCount() {
      return count;
    }

    public void setCount(Long count) {
      this.count = count;
    }

    public Double getFrom() {
      return from;
    }

    public void setFrom(Double from) {
      this.from = from;
    }

    public Double getTo() {
      return to;
    }

    public void setTo(Double to) {
      this.to = to;
    }
  }
}
