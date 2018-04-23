package com.blossomproject.core.common.search.facet;

import java.time.Instant;
import java.util.List;

public class DatesFacet extends Facet{

  public DatesFacet(String name, String path){
    super(FacetType.DATES, name, path);
  }

  private List<DatesFacetResult> results;

  public List<DatesFacetResult> getResults() {
    return results;
  }

  public void setResults(List<DatesFacetResult> results) {
    this.results = results;
  }

  public static class DatesFacetResult{
    private String term;
    private Instant from;
    private Instant to;
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

    public Instant getFrom() {
      return from;
    }

    public void setFrom(Instant from) {
      this.from = from;
    }

    public Instant getTo() {
      return to;
    }

    public void setTo(Instant to) {
      this.to = to;
    }
  }
}
