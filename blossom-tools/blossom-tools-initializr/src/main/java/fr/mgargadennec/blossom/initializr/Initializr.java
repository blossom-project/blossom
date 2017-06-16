package fr.mgargadennec.blossom.initializr;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Optional;

/**
 * Created by Maël Gargadennnec on 14/06/2017.
 */
@ConfigurationProperties("initializr")
public class Initializr {
  private List<Version> versions;
  private List<Group> groups;

  public List<Version> getVersions() {
    return versions;
  }

  public void setVersions(List<Version> versions) {
    this.versions = versions;
  }

  public List<Group> getGroups() {
    return groups;
  }

  public void setGroups(List<Group> groups) {
    this.groups = groups;
  }

  public Optional<Version> findVersion(String blossomVersion){
    return this.versions.stream().filter(v -> v.getBlossom().equals(blossomVersion)).findFirst();
  }

  public Optional<Dependency> findDependency(String dependencyId){
    return this.groups.stream().flatMap(g -> g.getDependencies().stream()).filter(d -> d.getId().equals(dependencyId)).findFirst();
  }
}
