package com.blossomproject.generator.configuration;

import com.google.common.base.Preconditions;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.configuration.model.impl.DefaultSettings;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SettingsBuilder {

  private GeneratorBuilder parent;
  private String projectRoot;
  private String basePackage;
  private String entityName;
  private FieldsBuilder fields;

  SettingsBuilder(GeneratorBuilder parent) {
    this.parent = parent;
    this.fields = new FieldsBuilder(this);
  }

  public SettingsBuilder projectRoot(String projectRoot) {
    this.projectRoot = projectRoot;
    return this;
  }

  public SettingsBuilder basePackage(String basePackage) {
    this.basePackage = basePackage;
    return this;
  }

  public SettingsBuilder entityName(String entityName) {
    this.entityName = entityName;
    return this;
  }

  public FieldsBuilder fields() {
    return this.fields;
  }

  public GeneratorBuilder done() {
    return parent;
  }


  private void validate() {
    Preconditions
      .checkState(this.projectRoot != null, "There must be a root path for the generator to work.");
    Path projectRoot = Paths.get(this.projectRoot);
    Preconditions.checkState(Files.exists(projectRoot) && Files.isDirectory(projectRoot),
      "The root path for the project must be an existing directory.");
    Preconditions.checkState(this.entityName != null, "The entity name must not be null.");
    Preconditions.checkState(this.basePackage != null,
      "There must be a base package for the generated classes.");
  }

  private void initialize() throws IOException {
    Files.createDirectories(Paths.get(this.projectRoot));
    Files.createDirectories(
      Paths.get(this.projectRoot).resolve("src").resolve("main").resolve("java"));
    Files.createDirectories(
      Paths.get(this.projectRoot).resolve("src").resolve("main").resolve("resources"));

  }

  public Settings build() {
    validate();
    try {
      initialize();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return new DefaultSettings(Paths.get(this.projectRoot), this.basePackage, this.entityName,
      this.fields.build());
  }
}
