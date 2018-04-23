package com.blossomproject.core.common.search.facet;

public class MinMaxFacet extends Facet{

  private Double min;
  private Double max;

  public MinMaxFacet(String name, String path) {
    super(FacetType.MIN_MAX, name, path);
  }

  public Double getMin() {
    return min;
  }

  public void setMin(Double min) {
    this.min = min;
  }

  public Double getMax() {
    return max;
  }

  public void setMax(Double max) {
    this.max = max;
  }
}
