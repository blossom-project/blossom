package com.blossomproject.generator.configuration.model.impl;

public class BlobField extends DefaultField {

  public BlobField(String name, String columnName, Class<?> className, String jdbcType,
    boolean required, boolean updatable, boolean nullable, String defaultValue,
    boolean searchable) {
    super(name, columnName, className, jdbcType, required, updatable, nullable, defaultValue,
      searchable);
  }

}
