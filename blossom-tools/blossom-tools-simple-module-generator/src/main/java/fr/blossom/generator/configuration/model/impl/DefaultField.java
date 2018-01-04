package fr.blossom.generator.configuration.model.impl;

import com.google.common.base.CaseFormat;
import fr.blossom.generator.configuration.model.Field;

public class DefaultField implements Field {

  protected final String name;
  protected final String columnName;
  protected final Class<?> className;
  protected final String jdbcType;
  protected final boolean required;
  protected final boolean updatable;
  protected final boolean nullable;
  protected final String defaultValue;
  protected final boolean searchable;

  public DefaultField(String name, String columnName, Class<?> className, String jdbcType,
    boolean required, boolean updatable, boolean nullable, String defaultValue,
    boolean searchable) {
    this.name = name;
    this.columnName = columnName;
    this.className = className;
    this.jdbcType = jdbcType;
    this.required = required;
    this.updatable = updatable;
    this.nullable = nullable;
    this.defaultValue = defaultValue;
    this.searchable = searchable;
  }

  public String getName() {
    return name;
  }

  public String getColumnName(){
    return columnName != null ? columnName : name;
  }

  public String getGetterName(){
    return  "get"+ CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
  }

  public String getSetterName(){
    return  "set"+ CaseFormat.LOWER_CAMEL.to(CaseFormat.UPPER_CAMEL, name);
  }

  public Class getClassName(){
    return className;
  }

  public boolean isSearchable(){
    return searchable;
  }

  public boolean isNullable(){
    return nullable;
  }

  public String getDefaultValue(){
    return defaultValue;
  }

  public boolean isRequiredCreate(){
    return required;
  }

  public boolean isPossibleUpdate(){
    return updatable;
  }

  public String getJdbcType(){
    return jdbcType;
  }
}
