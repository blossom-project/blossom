package com.blossom_project.generator.configuration;

import com.google.common.base.Preconditions;
import com.blossom_project.generator.configuration.model.Field;
import com.blossom_project.generator.configuration.model.impl.DefaultField;
import java.math.BigDecimal;

public class BigDecimalFieldBuilder extends FieldBuilder<BigDecimalFieldBuilder> {

  private final Integer precision;
  private final Integer fractionalPrecision;

  BigDecimalFieldBuilder(FieldsBuilder parent, String name, Integer precision,
    Integer fractionalPrecision) {
    super(parent, name, BigDecimal.class, "decimal(" + precision + "," + fractionalPrecision + ")");
    Preconditions.checkState(precision > 0);
    Preconditions.checkState(precision >= fractionalPrecision);
    this.precision = precision;
    this.fractionalPrecision = fractionalPrecision;
  }

  @Override
  Field build() {
    return new DefaultField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable);
  }
}
