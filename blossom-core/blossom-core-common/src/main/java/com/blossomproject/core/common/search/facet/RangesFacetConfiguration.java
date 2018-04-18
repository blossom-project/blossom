package com.blossomproject.core.common.search.facet;

import java.util.List;

public class RangesFacetConfiguration extends FacetConfiguration {

  private List<RangeConfiguration> ranges;

  public RangesFacetConfiguration() {
  }

  public RangesFacetConfiguration(String name) {
    super(name);
  }

  public RangesFacetConfiguration(String name, List<RangeConfiguration> ranges) {
    super(name);
    this.ranges = ranges;
  }

  public List<RangeConfiguration> getRanges() {
    return ranges;
  }

  public void setRanges(List<RangeConfiguration> ranges) {
    this.ranges = ranges;
  }

  public static class RangeConfiguration {

    private String name;
    private Double from;
    private Double to;

    public String getName() {
      return name;
    }

    public void setName(String name) {
      this.name = name;
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

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }

      RangeConfiguration that = (RangeConfiguration) o;

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

    RangesFacetConfiguration that = (RangesFacetConfiguration) o;

    return ranges != null ? ranges.equals(that.ranges) : that.ranges == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (ranges != null ? ranges.hashCode() : 0);
    return result;
  }
}
