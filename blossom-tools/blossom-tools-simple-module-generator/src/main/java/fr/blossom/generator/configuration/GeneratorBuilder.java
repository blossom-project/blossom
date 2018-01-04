package fr.blossom.generator.configuration;

import fr.blossom.generator.configuration.model.impl.DefaultGenerator;
import fr.blossom.generator.configuration.model.ExecutionPlan;
import fr.blossom.generator.configuration.model.Generator;
import fr.blossom.generator.configuration.model.Settings;

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
