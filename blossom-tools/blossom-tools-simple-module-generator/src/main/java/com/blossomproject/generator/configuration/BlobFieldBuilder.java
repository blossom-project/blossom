package com.blossomproject.generator.configuration;

import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.impl.BlobField;

public class BlobFieldBuilder extends FieldBuilder<BlobFieldBuilder> {

  BlobFieldBuilder(FieldsBuilder parent, String name) {
    super(parent, name, Byte[].class, "blob");
  }

  @Override
  Field build() {
    return new BlobField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable);
  }
}
