package com.blossom_project.generator.configuration.model.impl;

import com.blossom_project.generator.configuration.model.ExecutionPlan;
import com.blossom_project.generator.configuration.model.Generator;
import com.blossom_project.generator.configuration.model.Settings;
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
