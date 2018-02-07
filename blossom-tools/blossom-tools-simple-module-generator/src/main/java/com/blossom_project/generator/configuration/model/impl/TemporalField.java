package com.blossom_project.generator.configuration.model.impl;

import javax.persistence.TemporalType;

public class TemporalField extends DefaultField implements
  com.blossom_project.generator.configuration.model.TemporalField {

  private final TemporalType temporalType;

  public TemporalField(String name, String columnName, Class<?> className, String jdbcType,
    boolean required, boolean updatable, boolean nullable, String defaultValue,
    boolean searchable, TemporalType temporalType) {
    super(name, columnName, className, jdbcType, required, updatable, nullable, defaultValue,
      searchable);
    this.temporalType = temporalType;
  }

  @Override
  public TemporalType getTemporalType() {
    return temporalType;
  }
}
