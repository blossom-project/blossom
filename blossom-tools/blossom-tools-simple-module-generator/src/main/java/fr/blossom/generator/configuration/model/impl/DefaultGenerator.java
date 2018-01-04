package fr.blossom.generator.configuration.model.impl;

import fr.blossom.generator.configuration.model.ExecutionPlan;
import fr.blossom.generator.configuration.model.Generator;
import fr.blossom.generator.configuration.model.Settings;
import java.io.IOException;

public class DefaultGenerator implements Generator {

  private final Settings settings;
  private final ExecutionPlan executionPlan;

  public DefaultGenerator(Settings settings, ExecutionPlan executionPlan) {
    this.settings = settings;
    this.executionPlan = executionPlan;
  }

  public void generate() throws IOException {
    this.executionPlan.execute(this.settings);
  }

}
