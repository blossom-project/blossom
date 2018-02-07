package com.blossom_project.generator.configuration;

import com.blossom_project.generator.configuration.model.Field;
import com.blossom_project.generator.configuration.model.impl.DefaultField;
import javax.persistence.TemporalType;

public class BooleanFieldBuilder extends FieldBuilder<BooleanFieldBuilder> {

  private TemporalType temporalType;

  BooleanFieldBuilder(FieldsBuilder parent, String name) {
    super(parent, name, Boolean.class, "boolean");
  }

  @Override
  Field build() {
    return new DefaultField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable);
  }
}
