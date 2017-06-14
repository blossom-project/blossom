package fr.mgargadennec.blossom;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
public class ProjectConfiguration {
  private String groupId = "com.example";
  private String artifactId = "demo";
  private String name = "demo";
  private String description = "Demo project for Blossom";
  private String packageName = "com.example.demo";

  private String version = "";
  private List<String> dependencies = new ArrayList<>();

  public String getGroupId() {
    return groupId;
  }

  public void setGroupId(String groupId) {
    this.groupId = groupId;
  }

  public String getArtifactId() {
    return artifactId;
  }

  public void setArtifactId(String artifactId) {
    this.artifactId = artifactId;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  public String getPackageName() {
    return packageName;
  }

  public void setPackageName(String packageName) {
    this.packageName = packageName;
  }

  public String getVersion() {
    return version;
  }

  public void setVersion(String version) {
    this.version = version;
  }

  public List<String> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<String> dependencies) {
    this.dependencies = dependencies;
  }
}
