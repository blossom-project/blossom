package com.blossomproject.core.common.search.facet;

import java.util.List;
import java.util.Map;

public class GroupedTermsFacetConfiguration<T> extends FacetConfiguration {

  private Map<String, List<T>> groups;
  private Integer size;

  public GroupedTermsFacetConfiguration() {
  }

  public GroupedTermsFacetConfiguration(String name) {
    super(name);
  }

  public GroupedTermsFacetConfiguration(String name, Map<String, List<T>> groups) {
    super(name);
    this.groups=groups;
  }

  public Map<String, List<T>> getGroups() {
    return groups;
  }

  public void setGroups(Map<String, List<T>> groups) {
    this.groups = groups;
  }

  public Integer getSize() {
    return size;
  }

  public void setSize(Integer size) {
    this.size = size;
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

    GroupedTermsFacetConfiguration<?> that = (GroupedTermsFacetConfiguration<?>) o;

    if (groups != null ? !groups.equals(that.groups) : that.groups != null) {
      return false;
    }
    return size != null ? size.equals(that.size) : that.size == null;
  }

  @Override
  public int hashCode() {
    int result = super.hashCode();
    result = 31 * result + (groups != null ? groups.hashCode() : 0);
    result = 31 * result + (size != null ? size.hashCode() : 0);
    return result;
  }
}
