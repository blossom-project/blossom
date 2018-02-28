package com.blossomproject.generator.configuration;

import com.blossomproject.generator.classes.*;
import com.blossomproject.generator.resources.*;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.blossomproject.generator.configuration.model.ExecutionPlan;
import com.blossomproject.generator.configuration.model.impl.DefaultExecutionPlan;

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
  private ApiControllerGenerator apiControllerGenerator;
  private ConfigurationGenerator configurationGenerator;
  private IndexationJobGenerator indexationJobGenerator;

  private ElasticSearchSourceGenerator elasticSearchSourceGenerator;
  private ChangelogGenerator changelogGenerator;
  private MessagePropertiesGenerator messagesPropertiesGenerator;
  private ListViewGenerator listViewGenerator;
  private CreateViewGenerator createViewGenerator;
  private EntityViewGenerator entityViewGenerator;
  private EntityInformationsViewGenerator entityInformationsViewGenerator;
  private EditViewGenerator entityInformationsEditViewGenerator;

  ExecutionPlanBuilder(GeneratorBuilder parent) {
    this.parent = parent;
  }

  public ExecutionPlanBuilder allClasses() {
    return this.entity().repository().dao().dto().mapper().service().controller().apiController().configuration().indexationJob();
  }

  public ExecutionPlanBuilder allResources() {
    return this.messagesPropertiesGenerator().changelogGenerator().elasticSearchSourceGenerator().listViewGenerator().createViewGenerator().entityViewGenerator().entityInformationsViewGenerator().entityInformationsEditViewGenerator();
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

  public ExecutionPlanBuilder apiController() {
    this.apiControllerGenerator = new ApiControllerGenerator();
    return this;
  }

  public ExecutionPlanBuilder configuration() {
    this.configurationGenerator = new ConfigurationGenerator();
    return this;
  }

  public ExecutionPlanBuilder indexationJob() {
    this.indexationJobGenerator = new IndexationJobGenerator();
    return this;
  }

  public ExecutionPlanBuilder changelogGenerator() {
    this.changelogGenerator = new ChangelogGenerator();
    return this;
  }

  public ExecutionPlanBuilder elasticSearchSourceGenerator() {
    this.elasticSearchSourceGenerator = new ElasticSearchSourceGenerator();
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

  public ExecutionPlanBuilder entityViewGenerator() {
    this.entityViewGenerator = new EntityViewGenerator();
    return this;
  }

  public ExecutionPlanBuilder entityInformationsViewGenerator() {
    this.entityInformationsViewGenerator = new EntityInformationsViewGenerator();
    return this;
  }

  public ExecutionPlanBuilder entityInformationsEditViewGenerator() {
    this.entityInformationsEditViewGenerator = new EditViewGenerator();
    return this;
  }


  public ExecutionPlan build() {
    List<ClassGenerator> classGenerators = Lists
      .newArrayList(entityGenerator, repositoryGenerator, daoGenerator, daoImplGenerator,
        dtoGenerator, mapperGenerator, createFormGenerator, updateFormGenerator, serviceGenerator,
        serviceImplGenerator, controllerGenerator, apiControllerGenerator, configurationGenerator, indexationJobGenerator);
    Iterables.removeIf(classGenerators, Predicates.isNull());

    List<ResourceGenerator> resourceGenerators = Lists
      .newArrayList(changelogGenerator, elasticSearchSourceGenerator, messagesPropertiesGenerator, listViewGenerator, createViewGenerator, entityViewGenerator, entityInformationsViewGenerator, entityInformationsEditViewGenerator);
    Iterables.removeIf(resourceGenerators, Predicates.isNull());

    return new DefaultExecutionPlan(classGenerators, resourceGenerators);
  }
}
