package fr.blossom.generator;

import fr.blossom.generator.configuration.GeneratorBuilder;
import java.io.IOException;
import javax.persistence.TemporalType;

public class GeneratorExample {

  public static void main(String[] args) throws IOException {

    GeneratorBuilder builder = GeneratorBuilder.create();

    builder
      .settings()
      .basePackage("fr.blossom.sample.testmael")
      .entityName("TestMael")
      .projectRoot("D:\\dev\\mgargadennec\\blossom-project\\blossom-samples\\sample-complete\\")
      .fields()
        .defaultFields()
      ._string("test").maxLength(10).updatable(true).requiredCreate(true)
      .and()._date("date", TemporalType.DATE)
      .and()._date("time", TemporalType.TIME)
      .and()._date("timestamp", TemporalType.TIMESTAMP)
      .and()._date("timestamp_as_timestamp", TemporalType.TIMESTAMP).asTimestamp()
      .and()._boolean("bool").requiredCreate(true).updatable(true)
      .and()._integer("integer").requiredCreate(true).updatable(true)
      .and()._long("_long").requiredCreate(true).updatable(true)
      .and()._blob("blob").requiredCreate(true).overrideJdbcType("longblob")
      .and()._blob("blobl2").updatable(true)
      .and()._bigDecimal("bigDecimal", 15,6).nullable(true);

    builder.executionPlan().allClasses().allResources();

    builder.build().generate();

  }
}
