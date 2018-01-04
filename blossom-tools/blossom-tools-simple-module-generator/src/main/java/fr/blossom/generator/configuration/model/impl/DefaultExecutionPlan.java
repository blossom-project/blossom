package fr.blossom.generator.configuration.model.impl;

import com.google.common.collect.Maps;
import com.helger.jcodemodel.JCodeModel;
import fr.blossom.generator.classes.ClassGenerator;
import fr.blossom.generator.configuration.model.ExecutionPlan;
import fr.blossom.generator.configuration.model.Settings;
import fr.blossom.generator.resources.ResourceGenerator;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class DefaultExecutionPlan implements ExecutionPlan {

  private final List<ClassGenerator> classGenerators;
  private final List<ResourceGenerator> resourceGenerators;

  public DefaultExecutionPlan(List<ClassGenerator> classGenerators,
    List<ResourceGenerator> resourceGenerators) {
    this.classGenerators = classGenerators;
    this.resourceGenerators = resourceGenerators;
  }

  @Override
  public void execute(Settings settings) throws IOException {
    JCodeModel jCodeModel = new JCodeModel();

    classGenerators.stream().forEach(g -> {
      g.prepare(settings, jCodeModel);
      g.generate(settings, jCodeModel);
    });

    Map<String, String> parameters = Maps.newHashMap();
    resourceGenerators.stream().forEach(g -> g.generate(settings, parameters));

    jCodeModel.build(settings.getSrcPath().toFile());

  }
}
