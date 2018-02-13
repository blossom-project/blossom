package com.blossomproject.generator.configuration.model.impl;

import com.blossomproject.generator.configuration.model.ExecutionPlan;
import com.blossomproject.generator.configuration.model.Generator;
import com.blossomproject.generator.configuration.model.Settings;
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
