package fr.blossom.generator.configuration.model;

public interface Field {

  String getName();

  String getColumnName();

  String getGetterName();

  String getSetterName();

  Class getClassName();

  String getTemporalType();

  boolean isSearchable();

  boolean isNullable();

  boolean isNotBlank();

  Integer getMaxLength();

  String getDefaultValue();

  boolean isRequiredCreate();

  boolean isPossibleUpdate();

  String getJdbcType();

  boolean isLob();
}
