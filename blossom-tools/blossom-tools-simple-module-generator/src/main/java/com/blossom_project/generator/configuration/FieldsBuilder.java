package com.blossom_project.generator.configuration;

import com.google.common.collect.Lists;
import com.blossom_project.generator.configuration.model.Field;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.TemporalType;

public class FieldsBuilder {

  private final SettingsBuilder parent;
  private final List<FieldBuilder> fields;

  FieldsBuilder(SettingsBuilder parent) {
    this.parent = parent;
    this.fields = Lists.newArrayList();
  }

  public FieldsBuilder defaultFields() {
    return
      this._string("name").searchable(true).requiredCreate(true).updatable(true).nullable(false)
        .notBlank(true).defaultValue(null).maxLength(50)
        .and()
        ._string("description").searchable(true).requiredCreate(true).updatable(true)
        .nullable(false).notBlank(false).isLob(true).defaultValue(null)
        .and();
  }

  public StringFieldBuilder _string(String name) {
    StringFieldBuilder field = new StringFieldBuilder(this, name);
    fields.add(field);
    return field;
  }

  public TemporalFieldBuilder _date(String name, TemporalType temporalType) {
    TemporalFieldBuilder field = new TemporalFieldBuilder(this, name, temporalType);
    fields.add(field);
    return field;
  }

  public TemporalFieldBuilder _localDateTime(String name, TemporalType temporalType) {
    TemporalFieldBuilder field = new TemporalFieldBuilder(this, name, temporalType);
    fields.add(field);
    return field;
  }

  public BooleanFieldBuilder _boolean(String name) {
    BooleanFieldBuilder field = new BooleanFieldBuilder(this, name);
    fields.add(field);
    return field;
  }

  public LongFieldBuilder _long(String name) {
    LongFieldBuilder field = new LongFieldBuilder(this, name);
    fields.add(field);
    return field;
  }

  public IntegerFieldBuilder _integer(String name) {
    IntegerFieldBuilder field = new IntegerFieldBuilder(this, name);
    fields.add(field);
    return field;
  }

  public BigDecimalFieldBuilder _bigDecimal(String name, Integer precision, Integer fractionalPrecision) {
    BigDecimalFieldBuilder field = new BigDecimalFieldBuilder(this, name, precision,
      fractionalPrecision);
    fields.add(field);
    return field;
  }

  public BlobFieldBuilder _blob(String name) {
    BlobFieldBuilder field = new BlobFieldBuilder(this, name);
    fields.add(field);
    return field;
  }

  public SettingsBuilder done() {
    return parent;
  }

  List<Field> build() {
    return this.fields.stream().map(f -> f.build()).collect(Collectors.toList());
  }

}
