package fr.blossom.generator.configuration;

import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import fr.blossom.generator.classes.ClassGenerator;
import fr.blossom.generator.classes.ConfigurationGenerator;
import fr.blossom.generator.classes.ControllerGenerator;
import fr.blossom.generator.classes.CreateFormGenerator;
import fr.blossom.generator.classes.DaoGenerator;
import fr.blossom.generator.classes.DaoImplGenerator;
import fr.blossom.generator.classes.DtoGenerator;
import fr.blossom.generator.classes.EntityGenerator;
import fr.blossom.generator.classes.MapperGenerator;
import fr.blossom.generator.classes.RepositoryGenerator;
import fr.blossom.generator.classes.ServiceGenerator;
import fr.blossom.generator.classes.ServiceImplGenerator;
import fr.blossom.generator.classes.UpdateFormGenerator;
import fr.blossom.generator.configuration.model.impl.DefaultExecutionPlan;
import fr.blossom.generator.configuration.model.ExecutionPlan;
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

  ExecutionPlanBuilder(GeneratorBuilder parent) {
    this.parent = parent;
  }

  public ExecutionPlanBuilder all() {
    return this.entity().repository().dao().dto().mapper().service().controller().configuration();
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

  public ExecutionPlan build() {
    List<ClassGenerator> classGenerators = Lists
      .newArrayList(entityGenerator, repositoryGenerator, daoGenerator, daoImplGenerator,
        dtoGenerator, mapperGenerator, createFormGenerator, updateFormGenerator, serviceGenerator,
        serviceImplGenerator, controllerGenerator, configurationGenerator);
    Iterables.removeIf(classGenerators, Predicates.isNull());

    return new DefaultExecutionPlan(classGenerators, Lists.newArrayList());
  }
}
