package com.blossomproject.generator.configuration.model;

public interface StringField extends Field {

  boolean isNotBlank();

  Integer getMaxLength();

  boolean isLob();
}
