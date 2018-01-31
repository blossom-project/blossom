package fr.blossom.generator.configuration;

import fr.blossom.generator.configuration.model.Field;

@SuppressWarnings("unchecked")
public abstract class FieldBuilder<T extends FieldBuilder<T>> {

  private final FieldsBuilder parent;
  protected final String name;
  protected String columnName;
  protected Class<?> className;
  protected String jdbcType;
  protected boolean required;
  protected boolean updatable;
  protected boolean nullable;
  protected String defaultValue;
  protected boolean searchable;

  FieldBuilder(FieldsBuilder parent, String name, Class<?> className, String jdbcType) {
    this.parent = parent;
    this.name = name;
    this.className = className;
    this.jdbcType = jdbcType;
  }

  public T columnName(String columnName) {
    this.columnName = columnName;
    return (T) this;
  }

  public T requiredCreate(boolean required) {
    this.required = required;
    return (T) this;
  }

  public T updatable(boolean updatable) {
    this.updatable = updatable;
    return (T) this;
  }

  public T nullable(boolean nullable) {
    this.nullable = nullable;
    return (T) this;
  }

  public T defaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
    return (T) this;
  }

  public T searchable(boolean searchable) {
    this.searchable = searchable;
    return (T) this;
  }

  public T overrideJdbcType(String jdbcType) {
    this.jdbcType = jdbcType;
    return (T) this;
  }

  public FieldsBuilder and() {
    return parent;
  }

  abstract Field build();
}
