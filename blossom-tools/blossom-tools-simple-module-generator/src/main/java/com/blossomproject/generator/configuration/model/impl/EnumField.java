package com.blossomproject.generator.configuration.model.impl;

public class EnumField extends StringField {

  public EnumField(String name, String columnName, Class<?> className, String jdbcType, boolean required, boolean updatable, boolean nullable, String defaultValue, boolean searchable, int maxLength) {
    super(name, columnName, className, jdbcType, required, updatable, nullable, defaultValue, searchable, true, maxLength, false);
  }

}
