package com.blossomproject.generator.configuration;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.blossomproject.generator.classes.ClassGenerator;
import com.blossomproject.generator.classes.ConfigurationGenerator;
import com.blossomproject.generator.classes.ControllerGenerator;
import com.blossomproject.generator.classes.CreateFormGenerator;
import com.blossomproject.generator.classes.DaoGenerator;
import com.blossomproject.generator.classes.DaoImplGenerator;
import com.blossomproject.generator.classes.DtoGenerator;
import com.blossomproject.generator.classes.EntityGenerator;
import com.blossomproject.generator.classes.MapperGenerator;
import com.blossomproject.generator.classes.RepositoryGenerator;
import com.blossomproject.generator.classes.ServiceGenerator;
import com.blossomproject.generator.classes.ServiceImplGenerator;
import com.blossomproject.generator.classes.UpdateFormGenerator;
import com.blossomproject.generator.configuration.model.ExecutionPlan;
import com.blossomproject.generator.configuration.model.impl.DefaultExecutionPlan;
import com.blossomproject.generator.resources.ChangelogGenerator;
import com.blossomproject.generator.resources.CreateViewGenerator;
import com.blossomproject.generator.resources.ListViewGenerator;
import com.blossomproject.generator.resources.MessagePropertiesGenerator;
import com.blossomproject.generator.resources.ResourceGenerator;
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
    return this.messagesPropertiesGenerator().changelogGenerator().listViewGenerator().createViewGenerator();
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

  public ExecutionPlanBuilder createViewGenerator() {
    this.createViewGenerator = new CreateViewGenerator();
    return this;
  }


  public ExecutionPlan build() {
    List<ClassGenerator> classGenerators = Lists
      .newArrayList(entityGenerator, repositoryGenerator, daoGenerator, daoImplGenerator,
        dtoGenerator, mapperGenerator, createFormGenerator, updateFormGenerator, serviceGenerator,
        serviceImplGenerator, controllerGenerator, configurationGenerator);
    Iterables.removeIf(classGenerators, Predicates.isNull());

    List<ResourceGenerator> resourceGenerators = Lists
      .newArrayList(changelogGenerator, messagesPropertiesGenerator, listViewGenerator, createViewGenerator);
    Iterables.removeIf(resourceGenerators, Predicates.isNull());

    return new DefaultExecutionPlan(classGenerators, resourceGenerators);
  }
}
