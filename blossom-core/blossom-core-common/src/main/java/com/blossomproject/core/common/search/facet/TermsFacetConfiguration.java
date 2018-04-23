package com.blossomproject.core.common.search.facet;

import java.util.List;

public class TermsFacetConfiguration<T> extends FacetConfiguration {

  private static final Integer DEFAULT_SIZE = 10;

  private Integer size;
  private List<T> exclude;
  private List<T> include;

  public TermsFacetConfiguration() {
  }

  public TermsFacetConfiguration(String name) {
    super(name);
    this.size = DEFAULT_SIZE;
  }

  public TermsFacetConfiguration(String name, Integer size) {
    super(name);
    this.size = size;
  }

  public void setSize(Integer size) {
    this.size = size;
  }

  public Integer getSize() {
    return size;
  }

  public List<T> getExclude() {
    return exclude;
  }

  public void setExclude(List<T> exclude) {
    this.exclude = exclude;
  }

  public List<T> getInclude() {
    return include;
  }

  public void setInclude(List<T> include) {
    this.include = include;
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

    TermsFacetConfiguration<?> that = (TermsFacetConfiguration<?>) o;

    if (size != null ? !size.equals(that.size) : that.size != null) {
      return false;
    }
    if (exclude != null ? !exclude.equals(that.exclude) : that.exclude != null) {
      return false;
    }
    return include != null ? include.equals(that.include) : that.include == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (size != null ? size.hashCode() : 0);
    result = 31 * result + (exclude != null ? exclude.hashCode() : 0);
    result = 31 * result + (include != null ? include.hashCode() : 0);
    return result;
  }
}
