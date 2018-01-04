package fr.blossom.generator.configuration;

import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.impl.StringField;

public class StringFieldBuilder extends FieldBuilder<StringFieldBuilder> {

  private Integer maxLength;
  private boolean notBlank;
  private boolean isLob;

  StringFieldBuilder(FieldsBuilder parent, String name) {
    super(parent, name, String.class, "varchar");
  }

  public StringFieldBuilder maxLength(Integer maxLength) {
    this.maxLength = maxLength;
    this.jdbcType = "varchar(" + maxLength + ")";
    return this;
  }

  public StringFieldBuilder notBlank(boolean notBlank) {
    this.notBlank = notBlank;
    return this;
  }

  public StringFieldBuilder isLob(boolean isLob) {
    this.isLob = isLob;
    this.jdbcType = "clob";
    return this;
  }

  @Override
  Field build() {
    return new StringField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable, notBlank, maxLength, isLob);
  }
}
