package fr.blossom.generator.configuration;

import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.impl.DefaultField;

public class FloatFieldBuilder extends FieldBuilder<FloatFieldBuilder> {

  FloatFieldBuilder(FieldsBuilder parent, String name) {
    super(parent, name, Float.class, "real");
  }

  @Override
  Field build() {
    return new DefaultField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable);
  }
}
