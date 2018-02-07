package com.blossom_project.generator.configuration.model;

public interface StringField extends Field {

  boolean isNotBlank();

  Integer getMaxLength();

  boolean isLob();
}
