package com.blossom_project.generator.configuration;

import com.google.common.base.Preconditions;
import com.blossom_project.generator.configuration.model.Field;
import com.blossom_project.generator.configuration.model.impl.TemporalField;
import java.sql.Timestamp;
import java.util.Date;
import javax.persistence.TemporalType;

public class TemporalFieldBuilder extends FieldBuilder<TemporalFieldBuilder> {

  private TemporalType temporalType;

  TemporalFieldBuilder(FieldsBuilder parent, String name, TemporalType temporalType) {
    super(parent, name, String.class, "date");
    this.temporalType = temporalType;
    switch (temporalType) {
      case DATE:
        this.jdbcType = "date";
        this.className = Date.class;
        break;
      case TIME:
        this.jdbcType = "time";
        this.className = Date.class;
        break;
      case TIMESTAMP:
        this.jdbcType = "timestamp";
        this.className = Date.class;
        break;
    }
  }

  public TemporalFieldBuilder asTimestamp() {
    Preconditions.checkState(this.temporalType.equals(TemporalType.TIMESTAMP));
    this.className = Timestamp.class;
    this.temporalType = null;
    return this;
  }

  @Override
  Field build() {
    return new TemporalField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable, temporalType);
  }
}
