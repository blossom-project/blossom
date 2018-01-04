package fr.blossom.generator;

import fr.blossom.generator.configuration.GeneratorBuilder;
import java.io.IOException;

public class TestMain {

  public static void main(String[] args) throws IOException {

    GeneratorBuilder builder = GeneratorBuilder.create();

    builder
      .settings()
      .basePackage("fr.test.test")
      .entityName("TestMael")
      .projectRoot("D:\\dev\\mgargadennec\\test")
      .fields()
      .defaultFields()
      .string("test").maxLength(10).updatable(true).requiredCreate(true);

    builder.executionPlan().all();

    builder.build().generate();

  }
}
