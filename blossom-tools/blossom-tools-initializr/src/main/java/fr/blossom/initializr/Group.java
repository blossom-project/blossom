package fr.blossom.initializr;

import java.util.List;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
public class Group {
  private String name;
  private String description;

  private List<Dependency> dependencies;

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

  public List<Dependency> getDependencies() {
    return dependencies;
  }

  public void setDependencies(List<Dependency> dependencies) {
    this.dependencies = dependencies;
  }
}
