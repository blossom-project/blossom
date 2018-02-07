package com.blossom_project.generator.configuration.model.impl;

import com.google.common.collect.Maps;
import com.helger.jcodemodel.JCodeModel;
import com.blossom_project.generator.classes.ClassGenerator;
import com.blossom_project.generator.configuration.model.ExecutionPlan;
import com.blossom_project.generator.configuration.model.Settings;
import com.blossom_project.generator.resources.ResourceGenerator;
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

    jCodeModel.build(settings.getSrcPath().toFile());

    Map<String, String> parameters = Maps.newHashMap();
    parameters.put("ENTITY_NAME", settings.getEntityNameLowerUnderscore());
    parameters.put("ENTITY_NAME_PLURAL", settings.getEntityNameLowerUnderscore() + "s");
    parameters
      .put("PRIVILEGE_CREATE", "modules:" + settings.getEntityNameLowerUnderscore() + "s:create");
    parameters
      .put("LINK_ITEMS", "/blossom/modules/" + settings.getEntityNameLowerUnderscore() + "s");
    parameters.put("LINK_CREATE",
      "/blossom/modules/" + settings.getEntityNameLowerUnderscore() + "s/_create");
    parameters
      .put("LINK_ITEM", "/blossom/modules/" + settings.getEntityNameLowerUnderscore() + "s/{id}");
    parameters.put("ICON_PATH", "fa fa-question");

    resourceGenerators.stream().forEach(g -> {
      g.prepare(settings);
      g.generate(settings, parameters);
    });


  }
}
