package fr.blossom.generator.configuration;

import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.impl.DefaultField;

public class IntegerFieldBuilder extends FieldBuilder<IntegerFieldBuilder> {

  IntegerFieldBuilder(FieldsBuilder parent, String name) {
    super(parent, name, Integer.class, "integer");
  }

  @Override
  Field build() {
    return new DefaultField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable);
  }
}
