package fr.blossom.generator;

import fr.blossom.generator.configuration.GeneratorBuilder;
import java.io.IOException;
import javax.persistence.TemporalType;

public class Example {

  public static void main(String[] args) throws IOException {

    GeneratorBuilder builder = GeneratorBuilder.create();

    builder
      .settings()
      .basePackage("fr.test.test")
      .entityName("TestMael")
      .projectRoot("D:\\dev\\mgargadennec\\test")
      .fields()
      .defaultFields()
      ._string("test").maxLength(10).updatable(true).requiredCreate(true)
      .and()._date("date", TemporalType.DATE)
      .and()._date("time", TemporalType.TIME)
      .and()._date("timestamp", TemporalType.TIMESTAMP)
      .and()._date("timestamp_as_date", TemporalType.TIMESTAMP).asDate()
      .and()._date("timestamp_as_localdatetime", TemporalType.TIMESTAMP).asLocalDateTime().columnName("test_pouet")
      .and()._boolean("bool").requiredCreate(true).updatable(true)
      .and()._integer("integer").requiredCreate(true).updatable(true)
      .and()._long("_long").requiredCreate(true).updatable(true)
      .and()._blob("blob").requiredCreate(true)
      .and()._blob("blobl2").updatable(true)
      .and()._bigDecimal("bigDecimal").nullable(true)
      .and()._float("_float").nullable(true);

    builder.executionPlan().allClasses().allResources();

    builder.build().generate();

  }
}
