package com.blossomproject.generator.configuration;

import com.blossomproject.generator.configuration.model.ExecutionPlan;
import com.blossomproject.generator.configuration.model.Generator;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.configuration.model.impl.DefaultGenerator;

public class GeneratorBuilder {

  private SettingsBuilder settings;
  private ExecutionPlanBuilder executionPlan;

  private GeneratorBuilder() {
    this.settings = new SettingsBuilder(this);
    this.executionPlan = new ExecutionPlanBuilder(this);
  }

  public static GeneratorBuilder create() {
    return new GeneratorBuilder();
  }

  public SettingsBuilder settings() {
    return settings;
  }

  public ExecutionPlanBuilder executionPlan() {
    return executionPlan;
  }

  public Generator build() {
    Settings builtSettings = settings.build();
    ExecutionPlan builtExecutionPlan = executionPlan.build();
    return new DefaultGenerator(builtSettings, builtExecutionPlan);
  }
}
