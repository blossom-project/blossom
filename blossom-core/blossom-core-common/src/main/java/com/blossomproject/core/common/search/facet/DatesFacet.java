package com.blossomproject.core.common.search.facet;

import java.util.Date;
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
    private Date from;
    private Date to;
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

    public Date getFrom() {
      return from;
    }

    public void setFrom(Date from) {
      this.from = from;
    }

    public Date getTo() {
      return to;
    }

    public void setTo(Date to) {
      this.to = to;
    }
  }
}
