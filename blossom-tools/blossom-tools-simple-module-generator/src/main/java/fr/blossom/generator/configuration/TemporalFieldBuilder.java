package fr.blossom.generator.configuration;

import com.google.common.base.Preconditions;
import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.impl.TemporalField;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import javax.persistence.TemporalType;

public class TemporalFieldBuilder extends FieldBuilder<TemporalFieldBuilder> {

  private final TemporalType temporalType;

  TemporalFieldBuilder(FieldsBuilder parent, String name, TemporalType temporalType) {
    super(parent, name, String.class, "date");
    this.temporalType = temporalType;
    switch (temporalType) {
      case DATE:
        this.jdbcType = "date";
        this.className = LocalDate.class;
        break;
      case TIME:
        this.jdbcType = "time";
        this.className = LocalTime.class;
        break;
      case TIMESTAMP:
        this.jdbcType = "timestamp";
        this.className = Timestamp.class;
        break;
    }
  }

  public TemporalFieldBuilder asLocalDateTime() {
    Preconditions.checkState(this.temporalType.equals(TemporalType.TIMESTAMP));
    this.className = LocalDateTime.class;
    return this;
  }

  public TemporalFieldBuilder asDate() {
    Preconditions.checkState(this.temporalType.equals(TemporalType.TIMESTAMP));
    this.className = Date.class;
    return this;
  }

  @Override
  Field build() {
    return new TemporalField(name, columnName, className, jdbcType, required, updatable, nullable,
      defaultValue, searchable, temporalType);
  }
}
