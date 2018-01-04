package fr.blossom.generator.configuration;

import com.google.common.base.CaseFormat;
import fr.blossom.generator.configuration.model.Field;

public class StringFieldBuilder extends FieldBuilder<StringFieldBuilder> {

  private Integer maxLength;
  private boolean notBlank;
  private boolean isLob;

  StringFieldBuilder(FieldsBuilder parent, String name) {
    super(parent, name, String.class, "varchar");
  }

  public StringFieldBuilder maxLength(Integer maxLength) {
    this.maxLength = maxLength;
    return this;
  }

  public StringFieldBuilder notBlank(boolean notBlank) {
    this.notBlank = notBlank;
    return this;
  }

  public StringFieldBuilder isLob(boolean isLob) {
    this.isLob = isLob;
    return this;
  }

  @Override
  Field build() {
    return new Field() {
      @Override
      public String getName() {
        return name;
      }

      @Override
      public String getColumnName() {
        return columnName!=null ? columnName : name;
      }

      @Override
      public String getGetterName() {
        return "get" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
      }

      @Override
      public String getSetterName() {
        return "set" + CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
      }

      @Override
      public Class<?> getClassName() {
        return className;
      }

      @Override
      public String getTemporalType() {
        return null;
      }

      @Override
      public boolean isSearchable() {
        return searchable;
      }

      @Override
      public boolean isNullable() {
        return nullable;
      }

      @Override
      public boolean isNotBlank() {
        return notBlank;
      }

      @Override
      public Integer getMaxLength() {
        return maxLength;
      }

      @Override
      public String getDefaultValue() {
        return defaultValue;
      }

      @Override
      public boolean isRequiredCreate() {
        return required;
      }

      @Override
      public boolean isPossibleUpdate() {
        return updatable;
      }

      @Override
      public String getJdbcType() {
        return jdbcType;
      }

      @Override
      public boolean isLob() {
        return isLob;
      }
    };
  }
}
