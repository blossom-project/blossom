package fr.mgargadennec.blossom.simple_module_generator;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import fr.mgargadennec.blossom.simple_module_generator.generator.ConfigurationGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.ControllerGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.CreateFormGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.DaoGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.DaoImplGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.DtoGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.EntityGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.MapperGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.RepositoryGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.ServiceGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.ServiceImplGenerator;
import fr.mgargadennec.blossom.simple_module_generator.generator.UpdateFormGenerator;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
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
    buildResources(parameters, javaRoot);

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
    JDefinedClass repositoryClass = new RepositoryGenerator(entityClass).generate(parameters, codeModel);
    JDefinedClass daoClass = new DaoGenerator(entityClass).generate(parameters, codeModel);
    JDefinedClass daoImplClass = new DaoImplGenerator(entityClass,repositoryClass,daoClass).generate(parameters, codeModel);
    JDefinedClass dtoClass = new DtoGenerator().generate(parameters, codeModel);
    JDefinedClass createFormClass = new CreateFormGenerator().generate(parameters, codeModel);
    JDefinedClass updateFormClass = new UpdateFormGenerator().generate(parameters, codeModel);
    JDefinedClass serviceClass = new ServiceGenerator(dtoClass, createFormClass, updateFormClass).generate(parameters, codeModel);
    JDefinedClass mapperClass = new MapperGenerator(entityClass, dtoClass).generate(parameters, codeModel);
    JDefinedClass serviceImplClass = new ServiceImplGenerator( entityClass, daoClass, mapperClass, dtoClass, serviceClass,
      createFormClass, updateFormClass).generate(parameters, codeModel);
    JDefinedClass controllerClass = new ControllerGenerator(dtoClass, serviceClass,createFormClass, updateFormClass).generate(parameters, codeModel);
    JDefinedClass configurationClass = new ConfigurationGenerator(entityClass, repositoryClass, daoClass,daoImplClass, dtoClass, mapperClass, serviceClass, serviceImplClass, controllerClass).generate(parameters, codeModel);

    codeModel.build(javaRoot.toFile());
  }

  private void buildResources(Parameters parameters, Path javaRoot) {
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
