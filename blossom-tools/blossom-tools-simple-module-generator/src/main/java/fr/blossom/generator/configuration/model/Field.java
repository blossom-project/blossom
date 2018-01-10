package fr.blossom.generator.configuration.model;

public interface Field {

  String getName();

  String getColumnName();

  String getGetterName();

  String getSetterName();

  Class getClassName();

  boolean isSearchable();

  boolean isNullable();

  String getDefaultValue();

  boolean isRequiredCreate();

  boolean isPossibleUpdate();

  String getJdbcType();

}
