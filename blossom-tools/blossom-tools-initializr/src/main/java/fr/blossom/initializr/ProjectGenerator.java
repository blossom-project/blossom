package fr.blossom.initializr;

import com.squareup.kotlinpoet.*;
import com.sun.codemodel.CodeWriter;
import com.sun.codemodel.*;
import fr.blossom.initializr.enums.PACKAGING_MODE;
import fr.blossom.initializr.enums.SOURCE_LANGUAGE;
import org.apache.maven.model.*;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.apache.tomcat.util.http.fileupload.util.Streams;
import org.codehaus.plexus.util.xml.Xpp3Dom;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
public class ProjectGenerator {

  private final Initializr initializr;
  private final ResourceLoader resourceLoader;

  public ProjectGenerator(Initializr initializr, ResourceLoader resourceLoader) {
    this.initializr = initializr;
    this.resourceLoader = resourceLoader;
  }

  public void generateProject(ProjectConfiguration projectConfiguration, OutputStream os)
    throws Exception {
    ZipOutputStream zos = new ZipOutputStream(os);

    appendPom(projectConfiguration, zos);
    if (projectConfiguration.getSourceLanguage() == SOURCE_LANGUAGE.KOTLIN
      || projectConfiguration.getSourceLanguage() == SOURCE_LANGUAGE.JAVA_KOTLIN) {
      appendKotlinMain(projectConfiguration, zos);
    } else {
      appendJavaMain(projectConfiguration, zos);
    }
    appendProperties(projectConfiguration, zos);
    appendChangeLog(projectConfiguration, zos);

    zos.close();
  }

  private void appendChangeLog(ProjectConfiguration projectConfiguration, ZipOutputStream zos)
    throws IOException {
    Resource resource = this.resourceLoader
      .getResource("classpath:/changelog/db.changelog-master.yaml");

    ZipEntry e = new ZipEntry("src/main/resources/db/changelog/db.changelog-master.yaml");
    zos.putNextEntry(e);
    Streams.copy(resource.getInputStream(), zos, false);
  }

  private void appendPom(ProjectConfiguration projectConfiguration, ZipOutputStream zos)
    throws IOException {
    Version version = initializr.findVersion(projectConfiguration.getVersion()).get();

    Model model = new Model();
    model.setModelVersion("4.0.0");

    model.setGroupId(projectConfiguration.getGroupId());
    model.setArtifactId(projectConfiguration.getArtifactId());
    model.setVersion(projectConfiguration.getVersion());

    model.setPackaging(PACKAGING_MODE.JAR.name().toLowerCase());

    model.setName(projectConfiguration.getName());
    model.setDescription(projectConfiguration.getDescription());

    Properties properties = new Properties();
    properties.put("project.build.sourceEncoding", "UTF-8");
    properties.put("project.reporting.outputEncoding", "UTF-8");
    properties.put("java.version", "1.8");
    properties.put("springboot.version", version.getSpringboot());
    properties.put("blossom.version", version.getBlossom());
    model.setProperties(properties);

    org.apache.maven.model.Dependency blossomBom = new org.apache.maven.model.Dependency();
    blossomBom.setGroupId("fr.blossom");
    blossomBom.setArtifactId("blossom-parent");
    blossomBom.setVersion("${blossom.version}");
    blossomBom.setScope("import");
    blossomBom.setType("pom");

    DependencyManagement dependencyManagement = new DependencyManagement();
    dependencyManagement.addDependency(blossomBom);

    model.setDependencyManagement(dependencyManagement);

    model.setDependencies(
      projectConfiguration
        .getDependencies()
        .stream()
        .map(id -> initializr.findDependency(id))
        .filter(o -> o.isPresent()).map(o -> o.get()).map(d -> {
        org.apache.maven.model.Dependency dependency = new org.apache.maven.model.Dependency();
        dependency.setGroupId(d.getGroupId());
        dependency.setArtifactId(d.getArtifactId());
        dependency.setVersion("${blossom.version}");
        return dependency;
      })
        .collect(Collectors.toList())
    );

    if (PACKAGING_MODE.WAR.equals(projectConfiguration.getPackagingMode())) {
      model.setPackaging(PACKAGING_MODE.WAR.name().toLowerCase());
      properties.put("start.class", projectConfiguration.getPackageName() + ".Application");
      org.apache.maven.model.Dependency providedTomcat = new org.apache.maven.model.Dependency();
      providedTomcat.setGroupId("org.springframework.boot");
      providedTomcat.setArtifactId("spring-boot-starter-tomcat");
      providedTomcat.setScope("provided");
      model.getDependencies().add(providedTomcat);
    }

    Build build = new Build();

    Plugin queryDSLPlugin = new Plugin();
    queryDSLPlugin.setGroupId("com.mysema.maven");
    queryDSLPlugin.setArtifactId("apt-maven-plugin");
    queryDSLPlugin.setVersion("1.1.3");

    org.apache.maven.model.Dependency pluginDependency = new org.apache.maven.model.Dependency();
    pluginDependency.setGroupId("com.querydsl");
    pluginDependency.setArtifactId("querydsl-apt");
    pluginDependency.setVersion("4.1.4");
    List<org.apache.maven.model.Dependency> pluginDependencies = new ArrayList<org.apache.maven.model.Dependency>();
    pluginDependencies.add(pluginDependency);
    queryDSLPlugin.setDependencies(pluginDependencies);

    PluginExecution execution = new PluginExecution();
    execution.addGoal("process");

    Xpp3Dom configuration = new Xpp3Dom("configuration");
    Xpp3Dom outputDirectory = new Xpp3Dom("outputDirectory");
    outputDirectory.setValue("target/generated-sources");
    Xpp3Dom processor = new Xpp3Dom("processor");
    processor.setValue("com.querydsl.apt.jpa.JPAAnnotationProcessor");
    configuration.addChild(outputDirectory);
    configuration.addChild(processor);

    execution.setConfiguration(configuration);

    List<PluginExecution> executions = new ArrayList<>();
    executions.add(execution);
    queryDSLPlugin.setExecutions(executions);

    build.addPlugin(queryDSLPlugin);

    Plugin plugin = new Plugin();
    plugin.setGroupId("org.springframework.boot");
    plugin.setArtifactId("spring-boot-maven-plugin");
    plugin.setVersion("${springboot.version}");

    PluginExecution bootPluginExecution = new PluginExecution();
    bootPluginExecution.setGoals(Arrays.asList("repackage"));

    plugin.addExecution(bootPluginExecution);
    build.addPlugin(plugin);

    model.setBuild(build);

    if (projectConfiguration.getSourceLanguage() == SOURCE_LANGUAGE.JAVA_KOTLIN) {
      addMixedJavaKotlinConfiguration(model, version.getKotlin());
    } else if (projectConfiguration.getSourceLanguage() == SOURCE_LANGUAGE.KOTLIN) {
      addKotlinConfiguration(model, version.getKotlin());
    }

    ZipEntry e = new ZipEntry("pom.xml");
    zos.putNextEntry(e);
    new MavenXpp3Writer().write(zos, model);
  }

  private void addKotlinCommonConfiguration(Model pomModel, String kotlinVersion) {
    // Cf https://kotlinlang.org/docs/reference/using-maven.html for configurations
    // This configuration is based on the "mixed Java+Kotlin" Maven configuration

    pomModel.getProperties().put("kotlin.version", kotlinVersion);

    org.apache.maven.model.Dependency kotlinDependency = new org.apache.maven.model.Dependency();
    kotlinDependency.setGroupId("org.jetbrains.kotlin");
    kotlinDependency.setArtifactId("kotlin-stdlib");
    kotlinDependency.setVersion("${kotlin.version}");
    pomModel.getDependencies().add(kotlinDependency);
  }

  private void addMixedJavaKotlinConfiguration(Model pomModel, String kotlinVersion) {

    addKotlinCommonConfiguration(pomModel, kotlinVersion);

    Plugin kotlinPlugin = new Plugin();
    kotlinPlugin.setGroupId("org.jetbrains.kotlin");
    kotlinPlugin.setArtifactId("kotlin-maven-plugin");
    kotlinPlugin.setVersion("${kotlin.version}");

    {
      PluginExecution compileExecution = new PluginExecution();
      compileExecution.setId("compile");
      compileExecution.addGoal("compile");

      {
        Xpp3Dom configuration = new Xpp3Dom("configuration");
        Xpp3Dom sourceDirs = new Xpp3Dom("sourceDirs");
        Xpp3Dom sourceDirKotlin = new Xpp3Dom("sourceDir");
        sourceDirKotlin.setValue("${project.basedir}/src/main/kotlin");
        Xpp3Dom sourceDirJava = new Xpp3Dom("sourceDir");
        sourceDirJava.setValue("${project.basedir}/src/main/java");
        sourceDirs.addChild(sourceDirKotlin);
        sourceDirs.addChild(sourceDirJava);
        configuration.addChild(sourceDirs);
        compileExecution.setConfiguration(configuration);
      }

      PluginExecution testCompileExecution = new PluginExecution();
      testCompileExecution.setId("test-compile");
      testCompileExecution.addGoal("test-compile");

      {
        Xpp3Dom configuration = new Xpp3Dom("configuration");
        Xpp3Dom sourceDirs = new Xpp3Dom("sourceDirs");
        Xpp3Dom sourceDirKotlin = new Xpp3Dom("sourceDir");
        sourceDirKotlin.setValue("${project.basedir}/src/test/kotlin");
        Xpp3Dom sourceDirJava = new Xpp3Dom("sourceDir");
        sourceDirJava.setValue("${project.basedir}/src/test/java");
        sourceDirs.addChild(sourceDirKotlin);
        sourceDirs.addChild(sourceDirJava);
        configuration.addChild(sourceDirs);
        testCompileExecution.setConfiguration(configuration);
      }

      List<PluginExecution> executions = new ArrayList<>();
      executions.add(compileExecution);
      executions.add(testCompileExecution);
      kotlinPlugin.setExecutions(executions);
    }

    pomModel.getBuild().addPlugin(kotlinPlugin);

    Plugin mavenCompilePlugin = new Plugin();
    mavenCompilePlugin.setGroupId("org.apache.maven.plugins");
    mavenCompilePlugin.setArtifactId("maven-compiler-plugin");
    mavenCompilePlugin.setVersion("3.5.1");

    {
      PluginExecution defaultCompileExecution = new PluginExecution();
      defaultCompileExecution.setId("default-compile");
      defaultCompileExecution.setPhase("none");

      PluginExecution defaultTestCompileExecution = new PluginExecution();
      defaultTestCompileExecution.setId("default-testCompile");
      defaultTestCompileExecution.setPhase("none");

      PluginExecution javaCompileExecution = new PluginExecution();
      javaCompileExecution.setId("java-compile");
      javaCompileExecution.setPhase("compile");
      javaCompileExecution.addGoal("compile");

      PluginExecution javaTestCompileExecution = new PluginExecution();
      javaTestCompileExecution.setId("java-test-compile");
      javaTestCompileExecution.setPhase("test-compile");
      javaTestCompileExecution.addGoal("testCompile");

      List<PluginExecution> executions = new ArrayList<>();
      executions.add(defaultCompileExecution);
      executions.add(defaultTestCompileExecution);
      executions.add(javaCompileExecution);
      executions.add(javaTestCompileExecution);
      mavenCompilePlugin.setExecutions(executions);
    }

    pomModel.getBuild().addPlugin(mavenCompilePlugin);
  }

  private void addKotlinConfiguration(Model pomModel, String kotlinVersion) {

    addKotlinCommonConfiguration(pomModel, kotlinVersion);

    Plugin kotlinPlugin = new Plugin();
    kotlinPlugin.setGroupId("org.jetbrains.kotlin");
    kotlinPlugin.setArtifactId("kotlin-maven-plugin");
    kotlinPlugin.setVersion("${kotlin.version}");

    PluginExecution compileExecution = new PluginExecution();
    compileExecution.setId("compile");
    compileExecution.addGoal("compile");

    PluginExecution testCompileExecution = new PluginExecution();
    testCompileExecution.setId("test-compile");
    testCompileExecution.addGoal("test-compile");

    List<PluginExecution> executions = new ArrayList<>();
    executions.add(compileExecution);
    executions.add(testCompileExecution);
    kotlinPlugin.setExecutions(executions);

    pomModel.getBuild().addPlugin(kotlinPlugin);

    pomModel.getBuild().setSourceDirectory("${project.basedir}/src/main/kotlin");
    pomModel.getBuild().setTestSourceDirectory("${project.basedir}/src/main/kotlin");
  }

  private void appendJavaMain(ProjectConfiguration projectConfiguration, ZipOutputStream zos)
    throws IOException,
    JClassAlreadyExistsException {
    JCodeModel jc = new JCodeModel();
    JDefinedClass clazz = jc._class(projectConfiguration.getPackageName() + ".Application");

    clazz.annotate(SpringBootApplication.class);
    if (PACKAGING_MODE.WAR.equals(projectConfiguration.getPackagingMode())) {
      clazz._extends(SpringBootServletInitializer.class);
      JMethod configure = clazz.method(JMod.PROTECTED, SpringApplicationBuilder.class, "configure");
      configure.annotate(jc.ref(Override.class));
      JVar configureArguments = configure.param(SpringApplicationBuilder.class, "application");
      configure.body()._return(configureArguments.invoke("sources").arg(clazz.dotclass()));
    }

    JMethod main = clazz.method(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, jc.VOID, "main");
    JVar varargs = main.varParam(jc.ref(String.class), "args");

    main.body().add(
      jc._ref(SpringApplication.class).boxify().staticInvoke("run").arg(clazz.dotclass())
        .arg(varargs));

    jc.build(new CustomZipCodeWriter(zos));
  }

  private void appendKotlinMain(ProjectConfiguration projectConfiguration, ZipOutputStream zos) throws IOException {
    TypeSpec applicationClass = TypeSpec.classBuilder("Application")
      .addAnnotation(AnnotationSpec.builder(SpringBootApplication.class).build())
      .addModifiers(KModifier.OPEN)
      .build();

    FunSpec mainFun = FunSpec.builder("main")
      .addParameter(ParameterSpec.builder("args", new ClassName("", "Array<String>")).build())
      .addStatement("%T.run(Application::class.java, *args)", SpringApplication.class)
      .build();

    FileSpec file = FileSpec.builder(projectConfiguration.getPackageName(), "Application")
      .addType(applicationClass)
      .addFunction(mainFun)
      .build();

    String name = "src/main/kotlin/" + toDirName(projectConfiguration.getPackageName()) + "Application.kt";
    zos.putNextEntry(new ZipEntry(name));

    String fileContent = file.toString();
    zos.write(fileContent.getBytes(StandardCharsets.UTF_8));
    zos.closeEntry();

  }

  private void appendProperties(ProjectConfiguration projectConfiguration, ZipOutputStream zos)
    throws IOException {
    ZipEntry e = new ZipEntry("src/main/resources/application.properties");
    zos.putNextEntry(e);
  }

  private static class CustomZipCodeWriter extends CodeWriter {

    /**
     * @param target Zip file will be written to this stream.
     */
    public CustomZipCodeWriter(ZipOutputStream target) {
      zip = target;
      // nullify the close method.
      filter = new FilterOutputStream(zip) {
        public void close() {
        }
      };
    }

    private final ZipOutputStream zip;

    private final OutputStream filter;

    public OutputStream openBinary(JPackage pkg, String fileName) throws IOException {
      String name = fileName;
      if (!pkg.isUnnamed()) {
        name = "src/main/java/" + toDirName(pkg) + name;
      }

      zip.putNextEntry(new ZipEntry(name));
      return filter;
    }

    /**
     * Converts a package name to the directory name.
     */
    private static String toDirName(JPackage pkg) {
      return pkg.name().replace('.', '/') + '/';
    }

    public void close() throws IOException {
    }

  }

  private static String toDirName(String pkgName) {
    return pkgName.replace('.', '/') + '/';
  }

}
