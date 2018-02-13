package com.blossomproject.simple_module_generator;

import com.google.common.collect.Maps;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.blossomproject.simple_module_generator.classes.ConfigurationGenerator;
import com.blossomproject.simple_module_generator.classes.ControllerGenerator;
import com.blossomproject.simple_module_generator.classes.CreateFormGenerator;
import com.blossomproject.simple_module_generator.classes.DaoGenerator;
import com.blossomproject.simple_module_generator.classes.DaoImplGenerator;
import com.blossomproject.simple_module_generator.classes.DtoGenerator;
import com.blossomproject.simple_module_generator.classes.EntityGenerator;
import com.blossomproject.simple_module_generator.classes.MapperGenerator;
import com.blossomproject.simple_module_generator.classes.RepositoryGenerator;
import com.blossomproject.simple_module_generator.classes.ServiceGenerator;
import com.blossomproject.simple_module_generator.classes.ServiceImplGenerator;
import com.blossomproject.simple_module_generator.classes.UpdateFormGenerator;
import com.blossomproject.simple_module_generator.resources.ChangelogGenerator;
import com.blossomproject.simple_module_generator.resources.ListViewGenerator;
import com.blossomproject.simple_module_generator.resources.MessagePropertiesGenerator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Properties;
import java.util.Scanner;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;

public class BlossomToolsSimpleModuleGenerator {

  public static void main(String[] args) throws IOException {
    new BlossomToolsSimpleModuleGenerator().run(args);
  }

  public void run(String[] args) throws IOException {
    Scanner scanner = new Scanner(System.in);
    Parameters parameters = Parameters.readParametersInput(scanner);

    Path projectHome = fetchProjectHome();
    Path javaRoot = projectHome.resolve("src").resolve("main").resolve("java");
    Path resourceRoot = projectHome.resolve("src").resolve("main").resolve("resources");

    buildJavaClasses(parameters, javaRoot);
    buildResources(parameters, resourceRoot);

    mavenBuild(projectHome, false);
  }

  private Path fetchProjectHome() {
    String projectRoot = System.getProperty("project.root.path");

    if (projectRoot == null) {
      throw new RuntimeException("System property project.root.path is missing");
    }

    Path rootPath = Paths.get(projectRoot);
    if (!Files.exists(rootPath)) {
      throw new RuntimeException(
        "System property project.root.path is pointing to a non-existing location");
    }

    if (!Files.isDirectory(rootPath)) {
      throw new RuntimeException(
        "System property project.root.path is not a directory");
    }

    return rootPath;
  }


  private void buildJavaClasses(Parameters parameters, Path javaRoot) throws IOException {

    JCodeModel codeModel = new JCodeModel();

    JDefinedClass entityClass = new EntityGenerator().generate(parameters, codeModel);
    JDefinedClass repositoryClass = new RepositoryGenerator(entityClass)
      .generate(parameters, codeModel);
    JDefinedClass daoClass = new DaoGenerator(entityClass).generate(parameters, codeModel);
    JDefinedClass daoImplClass = new DaoImplGenerator(entityClass, repositoryClass, daoClass)
      .generate(parameters, codeModel);
    JDefinedClass dtoClass = new DtoGenerator().generate(parameters, codeModel);
    JDefinedClass createFormClass = new CreateFormGenerator().generate(parameters, codeModel);
    JDefinedClass updateFormClass = new UpdateFormGenerator().generate(parameters, codeModel);
    JDefinedClass serviceClass = new ServiceGenerator(dtoClass, createFormClass, updateFormClass)
      .generate(parameters, codeModel);
    JDefinedClass mapperClass = new MapperGenerator(entityClass, dtoClass)
      .generate(parameters, codeModel);
    JDefinedClass serviceImplClass = new ServiceImplGenerator(entityClass, daoClass, mapperClass,
      dtoClass, serviceClass,
      createFormClass, updateFormClass).generate(parameters, codeModel);
    JDefinedClass controllerClass = new ControllerGenerator(dtoClass, serviceClass, createFormClass,
      updateFormClass).generate(parameters, codeModel);
    JDefinedClass configurationClass = new ConfigurationGenerator(entityClass, repositoryClass,
      daoClass, daoImplClass, dtoClass, mapperClass, serviceClass, serviceImplClass,
      controllerClass).generate(parameters, codeModel);

    codeModel.build(javaRoot.toFile());
  }

  private void buildResources(Parameters parameters, Path resourceRoot) throws IOException {
    Map<String, String> params = Maps.newHashMap();
    params.put("ENTITY_NAME", parameters.getEntityNameLowerUnderscore());
    params.put("ENTITY_NAME_PLURAL", parameters.getEntityNameLowerUnderscore()+"s");
    params.put("PRIVILEGE_CREATE", "modules:"+parameters.getEntityNameLowerUnderscore()+"s:create");
    params.put("LINK_ITEMS", "/blossom/modules/"+parameters.getEntityNameLowerUnderscore()+"s");
    params.put("LINK_CREATE", "/blossom/modules/"+parameters.getEntityNameLowerUnderscore()+"s/_create");
    params.put("LINK_ITEM", "/blossom/modules/"+parameters.getEntityNameLowerUnderscore()+"s/{id}");
    params.put("ICON_PATH", "fa fa-question");


    Path templateRoot = resourceRoot.resolve("templates").resolve("modules").resolve(parameters.getEntityNameLowerUnderscore()+"s");
    Files.createDirectories(templateRoot);

    new ListViewGenerator().generate(templateRoot.resolve(parameters.getEntityNameLowerUnderscore()+"s.ftl"), parameters, params);

    Path messageRoot = resourceRoot.resolve("messages");
    Files.createDirectories(messageRoot);

    new MessagePropertiesGenerator().generate(messageRoot.resolve(parameters.getEntityNameLowerHyphen()+".properties"), parameters,params);
    new MessagePropertiesGenerator().generate(messageRoot.resolve(parameters.getEntityNameLowerHyphen()+"_fr.properties"), parameters,params);
    new MessagePropertiesGenerator().generate(messageRoot.resolve(parameters.getEntityNameLowerHyphen()+"_en.properties"), parameters,params);


    Path changelogRoot = resourceRoot.resolve("db").resolve("changelog").resolve("generated");
    Files.createDirectories(changelogRoot);

    new ChangelogGenerator().generate(changelogRoot.resolve("4_db.changelog_blossom_generated_"+parameters.getEntityNameLowerUnderscore()+".xml"), parameters,params);
  }

  private void mavenBuild(Path directory, Boolean skipTests) {
    try {
      InvocationRequest request = new DefaultInvocationRequest();
      request.setPomFile(directory.resolve("pom.xml").toFile());
      request.setGoals(Arrays.asList("clean", "install"));

      Properties prop = new Properties();
      prop.put("skipTests", skipTests.toString());
      request.setProperties(prop);

      Invoker invoker = new DefaultInvoker();
      invoker.execute(request);
    } catch (MavenInvocationException e) {
      System.err
        .println(
          "Can't run 'maven clean install' without error command on newly created project. Do it manually.");
      e.printStackTrace();
    }
  }


}
