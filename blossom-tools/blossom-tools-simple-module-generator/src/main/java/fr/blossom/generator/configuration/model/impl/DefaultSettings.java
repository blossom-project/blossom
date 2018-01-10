package fr.blossom.generator.configuration.model.impl;

import com.google.common.base.CaseFormat;
import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.Settings;
import java.nio.file.Path;
import java.util.List;

public class DefaultSettings implements Settings {

  private final Path projectRoot;
  private final String basePackage;
  private final String entityName;
  private final List<Field> fields;

  public DefaultSettings(Path projectRoot, String basePackage, String entityName,
    List<Field> fields) {
    this.projectRoot = projectRoot;
    this.basePackage = basePackage;
    this.entityName = entityName;
    this.fields = fields;
  }

  @Override
  public Path getSrcPath() {
    return this.projectRoot.resolve("src").resolve("main").resolve("java");
  }

  @Override
  public Path getResourcePath() {
    return this.projectRoot.resolve("src").resolve("main").resolve("resources");
  }

  @Override
  public String getBasePackage() {
    return this.basePackage;
  }

  @Override
  public String getEntityName() {
    return this.entityName;
  }

  @Override
  public String getEntityNameUpperUnderscore() {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.UPPER_UNDERSCORE, this.entityName);
  }

  @Override
  public String getEntityNameLowerUnderscore() {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, this.entityName);
  }

  @Override
  public String getEntityNameLowerCamel() {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_CAMEL, this.entityName);
  }

  @Override
  public String getEntityNameLowerHyphen() {
    return CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_HYPHEN, this.entityName);
  }

  @Override
  public List<Field> getFields() {
    return this.fields;
  }
}
