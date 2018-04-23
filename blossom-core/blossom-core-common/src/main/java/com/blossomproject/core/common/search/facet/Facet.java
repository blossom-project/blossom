package com.blossomproject.core.common.search.facet;

public abstract class Facet {

  private FacetType type;
  private String name;
  private String path;
  private boolean multiple;

  public Facet(FacetType type, String name, String path) {
    this.type = type;
    this.name = name;
    this.path = path;
  }

  public FacetType getType() {
    return type;
  }

  public void setType(FacetType type) {
    this.type = type;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public void setPath(String path) {
    this.path = path;
  }

  public String getPath() {
    return path;
  }

  public boolean isMultiple() {
    return multiple;
  }

  public void setMultiple(boolean multiple) {
    this.multiple = multiple;
  }

  public enum FacetType {
    TERMS, GROUPED_TERMS, MIN_MAX, DATES, RANGES;
  }
}
