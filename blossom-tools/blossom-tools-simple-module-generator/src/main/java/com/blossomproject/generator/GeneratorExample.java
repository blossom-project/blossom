package com.blossomproject.generator;

import com.blossomproject.generator.configuration.GeneratorBuilder;

import java.io.IOException;
import javax.persistence.TemporalType;

public class GeneratorExample {

  public static void main(String[] args) throws IOException {

    GeneratorBuilder builder = GeneratorBuilder.create();

    builder
      .settings()
      .basePackage("com.blossomproject.sample.testmael")
      .entityName("TestMael")
      .projectRoot("D:\\dev\\mgargadennec\\blossom-project\\blossom-samples\\sample-complete\\")
      .fields().defaultFields()
      ._string("string").maxLength(10).updatable(true).requiredCreate(true).updatable(true).searchable(true)
            .and()._string("string2").maxLength(10).requiredCreate(false).updatable(true).nullable(true).notBlank(true).searchable(true)
      .and()._date("date", TemporalType.DATE).requiredCreate(true).nullable(true).updatable(true)
      //.and()._date("time", TemporalType.TIME).requiredCreate(true).updatable(true)
      .and()._date("timestamp", TemporalType.TIMESTAMP).requiredCreate(true).updatable(true)
            .and()._date("timestamp_as_timestamp", TemporalType.TIMESTAMP).asTimestamp().nullable(true)
      .and()._boolean("bool").requiredCreate(true).updatable(true)
            .and()._boolean("bool2").requiredCreate(false).updatable(true).nullable(true)
     /* .and()._integer("integer").requiredCreate(true).updatable(true)
      .and()._long("_long").nullable(true)
      .and()._blob("blob").nullable(true).overrideJdbcType("longblob")
      .and()._blob("blobl2").nullable(true)
      .and()._bigDecimal("bigDecimal", 15,6).nullable(true)*/
    .and()._enum("enumtest", Day.class).requiredCreate(true).updatable(true).searchable(true)
    .and()._enum("ville", VilleEnum.class).requiredCreate(false).updatable(true).nullable(true).searchable(true);


      builder.executionPlan().allClasses().allResources();

    builder.build().generate();

  }

    public enum Day {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY,
        THURSDAY, FRIDAY, SATURDAY
    }

    public enum VilleEnum {
        MARSEILLE,
        TOULOUSE,
        LYON,
        CAEN
    }

}

