package fr.blossom.generator.configuration;

import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.impl.DefaultField;
import javax.persistence.TemporalType;

public class BlobFieldBuilder extends FieldBuilder<BlobFieldBuilder> {

  private TemporalType temporalType;

  BlobFieldBuilder(FieldsBuilder parent, String name) {
    super(parent, name, Byte[].class, "blob");
  }

  @Override
  Field build() {
    return new DefaultField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable);
  }
}
