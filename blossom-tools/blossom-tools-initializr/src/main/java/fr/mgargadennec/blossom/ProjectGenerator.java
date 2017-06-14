package fr.mgargadennec.blossom;

import com.sun.codemodel.*;
import org.apache.maven.model.Build;
import org.apache.maven.model.Model;
import org.apache.maven.model.Parent;
import org.apache.maven.model.Plugin;
import org.apache.maven.model.io.xpp3.MavenXpp3Writer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
public class ProjectGenerator {
  private final Initializr initializr;

  public ProjectGenerator(Initializr initializr) {
    this.initializr = initializr;
  }

  public void generateProject(ProjectConfiguration projectConfiguration, OutputStream os) throws Exception {
    ZipOutputStream zos = new ZipOutputStream(os);

    appendPom(projectConfiguration, zos);
    appendMain(projectConfiguration, zos);

    zos.close();
  }

  private void appendPom(ProjectConfiguration projectConfiguration, ZipOutputStream zos) throws IOException {
    Version version = initializr.findVersion(projectConfiguration.getVersion()).get();

    Model model = new Model();
    model.setModelVersion("4.0.0");

    Parent parent = new Parent();
    parent.setGroupId("org.springframework.boot");
    parent.setArtifactId("spring-boot-starter-parent");
    parent.setVersion(version.getSpringboot());
    parent.setRelativePath(null);
    model.setParent(parent);

    model.setGroupId(projectConfiguration.getGroupId());
    model.setArtifactId(projectConfiguration.getArtifactId());
    model.setVersion(projectConfiguration.getVersion());

    model.setPackaging("jar");

    model.setName(projectConfiguration.getName());
    model.setDescription(projectConfiguration.getDescription());

    Properties properties = new Properties();
    properties.put("project.build.sourceEncoding", "UTF-8");
    properties.put("project.reporting.outputEncoding", "UTF-8");
    properties.put("java.version", "1.8");
    model.setProperties(properties);

    model.setDependencies(projectConfiguration.getDependencies().stream()
      .map(id -> initializr.findDependency(id))
      .filter(o -> o.isPresent())
      .map(o -> o.get())
      .map(d -> {
        org.apache.maven.model.Dependency dependency = new org.apache.maven.model.Dependency();
        dependency.setGroupId(d.getGroupId());
        dependency.setArtifactId(d.getArtifactId());
        dependency.setVersion(version.getBlossom());
        return dependency;
      }).collect(Collectors.toList()));

    Build build = new Build();
    Plugin plugin = new Plugin();
    plugin.setGroupId("org.springframework.boot");
    plugin.setArtifactId("spring-boot-maven-plugin");
    build.addPlugin(plugin);

    model.setBuild(build);


    ZipEntry e = new ZipEntry("pom.xml");
    zos.putNextEntry(e);
    new MavenXpp3Writer().write(zos, model);
  }


  private void appendMain(ProjectConfiguration projectConfiguration, ZipOutputStream zos) throws IOException, JClassAlreadyExistsException {
    JCodeModel jc = new JCodeModel();
    JDefinedClass clazz = jc._class(projectConfiguration.getPackageName() + ".Application");

    clazz.annotate(EnableAutoConfiguration.class);
    clazz.annotate(jc.ref("fr.mgargadennec.blossom.autoconfigure.EnableBlossom"));

    JMethod main = clazz.method(JMod.PUBLIC | JMod.FINAL | JMod.STATIC, jc.VOID, "main");
    JVar varargs = main.varParam(jc.ref(String.class), "args");

    main.body().add(jc._ref(SpringApplication.class).boxify().staticInvoke("run").arg(clazz.dotclass()).arg(varargs));

    jc.build(new CustomZipCodeWriter(zos));
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
      if (!pkg.isUnnamed()) name = "src/main/java/"+toDirName(pkg) + name;

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

}
