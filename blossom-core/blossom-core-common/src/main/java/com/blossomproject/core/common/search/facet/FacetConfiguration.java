package com.blossomproject.core.common.search.facet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "facet")
@JsonSubTypes({
  @Type(name = "TERMS", value = TermsFacetConfiguration.class),
  @Type(name = "MIN_MAX", value = MinMaxFacetConfiguration.class),
  @Type(name = "GROUPED_TERMS", value = GroupedTermsFacetConfiguration.class),
  @Type(name = "DATES", value = DatesFacetConfiguration.class),
  @Type(name = "RANGES", value = RangesFacetConfiguration.class)})
public abstract class FacetConfiguration {

  private String name;
  private String uuid;
  private boolean global = true;

  public FacetConfiguration() {
  }

  public FacetConfiguration(String name) {
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  public String getUuid() {
    return uuid;
  }

  public void setUuid(String uuid) {
    this.uuid = uuid;
  }

  public void setGlobal(boolean global) {
    this.global = global;
  }

  public Boolean isGlobal() {
    return global;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }

    FacetConfiguration that = (FacetConfiguration) o;

    if (global != that.global) {
      return false;
    }
    if (name != null ? !name.equals(that.name) : that.name != null) {
      return false;
    }
    return uuid != null ? uuid.equals(that.uuid) : that.uuid == null;
  }

  @Override
  public int hashCode() {
    int result = name != null ? name.hashCode() : 0;
    result = 31 * result + (uuid != null ? uuid.hashCode() : 0);
    result = 31 * result + (global ? 1 : 0);
    return result;
  }
}
