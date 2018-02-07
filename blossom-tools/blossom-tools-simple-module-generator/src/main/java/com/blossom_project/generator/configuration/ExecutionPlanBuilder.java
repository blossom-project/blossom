package com.blossom_project.generator.configuration;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.blossom_project.generator.classes.ClassGenerator;
import com.blossom_project.generator.classes.ConfigurationGenerator;
import com.blossom_project.generator.classes.ControllerGenerator;
import com.blossom_project.generator.classes.CreateFormGenerator;
import com.blossom_project.generator.classes.DaoGenerator;
import com.blossom_project.generator.classes.DaoImplGenerator;
import com.blossom_project.generator.classes.DtoGenerator;
import com.blossom_project.generator.classes.EntityGenerator;
import com.blossom_project.generator.classes.MapperGenerator;
import com.blossom_project.generator.classes.RepositoryGenerator;
import com.blossom_project.generator.classes.ServiceGenerator;
import com.blossom_project.generator.classes.ServiceImplGenerator;
import com.blossom_project.generator.classes.UpdateFormGenerator;
import com.blossom_project.generator.configuration.model.ExecutionPlan;
import com.blossom_project.generator.configuration.model.impl.DefaultExecutionPlan;
import com.blossom_project.generator.resources.ChangelogGenerator;
import com.blossom_project.generator.resources.CreateViewGenerator;
import com.blossom_project.generator.resources.ListViewGenerator;
import com.blossom_project.generator.resources.MessagePropertiesGenerator;
import com.blossom_project.generator.resources.ResourceGenerator;
import java.util.List;

public class ExecutionPlanBuilder {

  private GeneratorBuilder parent;

  private EntityGenerator entityGenerator;
  private RepositoryGenerator repositoryGenerator;
  private DaoGenerator daoGenerator;
  private DaoImplGenerator daoImplGenerator;
  private DtoGenerator dtoGenerator;
  private MapperGenerator mapperGenerator;
  private CreateFormGenerator createFormGenerator;
  private UpdateFormGenerator updateFormGenerator;
  private ServiceGenerator serviceGenerator;
  private ServiceImplGenerator serviceImplGenerator;
  private ControllerGenerator controllerGenerator;
  private ConfigurationGenerator configurationGenerator;

  private ChangelogGenerator changelogGenerator;
  private MessagePropertiesGenerator messagesPropertiesGenerator;
  private ListViewGenerator listViewGenerator;
  private CreateViewGenerator createViewGenerator;

  ExecutionPlanBuilder(GeneratorBuilder parent) {
    this.parent = parent;
  }

  public ExecutionPlanBuilder allClasses() {
    return this.entity().repository().dao().dto().mapper().service().controller().configuration();
  }

  public ExecutionPlanBuilder allResources() {
    return this.messagesPropertiesGenerator().changelogGenerator().listViewGenerator();
  }

  public ExecutionPlanBuilder entity() {
    this.entityGenerator = new EntityGenerator();
    return this;
  }

  public ExecutionPlanBuilder repository() {
    this.repositoryGenerator = new RepositoryGenerator();
    return this;
  }

  public ExecutionPlanBuilder dao() {
    this.daoGenerator = new DaoGenerator();
    this.daoImplGenerator = new DaoImplGenerator();
    return this;
  }

  public ExecutionPlanBuilder dto() {
    this.dtoGenerator = new DtoGenerator();
    return this;
  }

  public ExecutionPlanBuilder mapper() {
    this.mapperGenerator = new MapperGenerator();
    return this;
  }

  public ExecutionPlanBuilder service() {
    this.serviceGenerator = new ServiceGenerator();
    this.serviceImplGenerator = new ServiceImplGenerator();
    this.createFormGenerator = new CreateFormGenerator();
    this.updateFormGenerator = new UpdateFormGenerator();
    return this;
  }

  public ExecutionPlanBuilder controller() {
    this.controllerGenerator = new ControllerGenerator();
    return this;
  }

  public ExecutionPlanBuilder configuration() {
    this.configurationGenerator = new ConfigurationGenerator();
    return this;
  }

  public ExecutionPlanBuilder changelogGenerator() {
    this.changelogGenerator = new ChangelogGenerator();
    return this;
  }

  public ExecutionPlanBuilder messagesPropertiesGenerator() {
    this.messagesPropertiesGenerator = new MessagePropertiesGenerator();
    return this;
  }

  public ExecutionPlanBuilder listViewGenerator() {
    this.listViewGenerator = new ListViewGenerator();
    return this;
  }


  public ExecutionPlan build() {
    List<ClassGenerator> classGenerators = Lists
      .newArrayList(entityGenerator, repositoryGenerator, daoGenerator, daoImplGenerator,
        dtoGenerator, mapperGenerator, createFormGenerator, updateFormGenerator, serviceGenerator,
        serviceImplGenerator, controllerGenerator, configurationGenerator);
    Iterables.removeIf(classGenerators, Predicates.isNull());

    List<ResourceGenerator> resourceGenerators = Lists
      .newArrayList(changelogGenerator, messagesPropertiesGenerator, listViewGenerator);
    Iterables.removeIf(resourceGenerators, Predicates.isNull());

    return new DefaultExecutionPlan(classGenerators, resourceGenerators);
  }
}
