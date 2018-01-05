package fr.blossom.generator.configuration;

import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.impl.DefaultField;
import java.math.BigDecimal;

public class BigDecimalFieldBuilder extends FieldBuilder<BigDecimalFieldBuilder> {

  BigDecimalFieldBuilder(FieldsBuilder parent, String name) {
    super(parent, name, BigDecimal.class, "decimal");
  }

  @Override
  Field build() {
    return new DefaultField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable);
  }
}
