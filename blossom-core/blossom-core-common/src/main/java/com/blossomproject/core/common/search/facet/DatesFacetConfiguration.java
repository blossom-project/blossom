package com.blossomproject.core.common.search.facet;

import java.time.Instant;
import java.util.List;

public class DatesFacetConfiguration extends FacetConfiguration {

  private List<PeriodConfiguration> periods;

  public DatesFacetConfiguration() {
  }

  public DatesFacetConfiguration(String name) {
    super(name);
  }

  public DatesFacetConfiguration(String name, List<PeriodConfiguration> periods) {
    super(name);
    this.periods = periods;
  }

  public List<PeriodConfiguration> getPeriods() {
    return periods;
  }

  public void setPeriods(List<PeriodConfiguration> periods) {
    this.periods = periods;
  }

  public static class PeriodConfiguration {

    private String name;
    private Instant from;
    private Instant to;

    public PeriodConfiguration(){}

    public PeriodConfiguration(String name, Instant from, Instant to) {
      this.name = name;
      this.from = from;
      this.to = to;
    }

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
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

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      PeriodConfiguration that = (PeriodConfiguration) o;

      if (name != null ? !name.equals(that.name) : that.name != null) {
        return false;
      }
      if (from != null ? !from.equals(that.from) : that.from != null) {
        return false;
      }
      return to != null ? to.equals(that.to) : that.to == null;
    }

    @Override
    public int hashCode() {
      int result = name != null ? name.hashCode() : 0;
      result = 31 * result + (from != null ? from.hashCode() : 0);
      result = 31 * result + (to != null ? to.hashCode() : 0);
      return result;
    }
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    if (!super.equals(o)) {
      return false;
    }

    DatesFacetConfiguration that = (DatesFacetConfiguration) o;

    return periods != null ? periods.equals(that.periods) : that.periods == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (periods != null ? periods.hashCode() : 0);
    return result;
  }
}
