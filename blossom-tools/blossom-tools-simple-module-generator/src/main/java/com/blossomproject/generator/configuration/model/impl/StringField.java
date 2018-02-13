package com.blossomproject.generator.configuration.model.impl;

public class StringField extends DefaultField implements
  com.blossomproject.generator.configuration.model.StringField {

  private final Boolean notBlank;
  private final Integer maxLength;
  private final Boolean lob;


  public StringField(String name, String columnName, Class<?> className, String jdbcType,
    boolean required, boolean updatable, boolean nullable, String defaultValue,
    boolean searchable, boolean notBlank, Integer maxLength, boolean lob) {
    super(name, columnName, className, jdbcType, required, updatable, nullable, defaultValue,
      searchable);
    this.notBlank = notBlank;
    this.maxLength = maxLength;
    this.lob = lob;
  }

  @Override
  public boolean isNotBlank() {
    return notBlank;
  }

  @Override
  public Integer getMaxLength() {
    return maxLength;
  }

  @Override
  public boolean isLob() {
    return lob;
  }
}
