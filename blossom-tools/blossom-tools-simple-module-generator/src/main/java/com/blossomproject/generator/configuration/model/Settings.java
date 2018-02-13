package com.blossomproject.generator.configuration.model;

import java.nio.file.Path;
import java.util.List;

public interface Settings {

  Path getSrcPath();

  Path getResourcePath();

  String getBasePackage();

  String getEntityName();

  String getEntityNameUpperUnderscore();

  String getEntityNameLowerUnderscore();

  String getEntityNameLowerCamel();

  String getEntityNameLowerHyphen();

  List<Field> getFields();

}
