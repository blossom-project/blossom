package com.blossomproject.generator.utils;

import com.blossomproject.generator.configuration.model.Settings;

public class GeneratorUtils {

  public static String getEntityClassName(Settings settings) {
    return settings.getEntityName();
  }

  public static String getRepositoryClassName(Settings settings) {
    return settings.getEntityName() + "Repository";
  }

  public static String getDaoClassName(Settings settings) {
    return  settings.getEntityName() + "Dao";
  }

  public static String getDaoImplClassName(Settings settings) {
    return settings.getEntityName() + "DaoImpl";
  }

  public static String getServiceClassName(Settings settings) {
    return  settings.getEntityName() + "Service";
  }

  public static String getServiceImplClassName(Settings settings) {
    return  settings.getEntityName() + "ServiceImpl";
  }

  public static String getDtoClassName(Settings settings) {
    return  settings.getEntityName() + "DTO";
  }

  public static String getMapperClassName(Settings settings) {
    return  settings.getEntityName() + "DTOMapper";
  }

  public static String getCreateFormClassName(Settings settings) {
    return  settings.getEntityName() + "CreateForm";
  }

  public static String getUpdateFormClassName(Settings settings) {
    return  settings.getEntityName() + "UpdateForm";
  }

  public static String getControllerClassName(Settings settings) {
    return  settings.getEntityName() + "Controller";
  }

  public static String getConfigurationClassName(Settings settings) {
    return  settings.getEntityName() + "Configuration";
  }

  public static String getEntityFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getEntityClassName(settings);
  }

  public static String getRepositoryFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getRepositoryClassName(settings);
  }

  public static String getDaoFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getDaoClassName(settings);
  }

  public static String getDaoImplFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getDaoImplClassName(settings);
  }

  public static String getServiceFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getServiceClassName(settings);
  }

  public static String getServiceImplFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getServiceImplClassName(settings);
  }

  public static String getDtoFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getDtoClassName(settings);
  }

  public static String getCreateFormFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getCreateFormClassName(settings);
  }

  public static String getUpdateFormFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getUpdateFormClassName(settings);
  }

  public static String getMapperFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getMapperClassName(settings);
  }

  public static String getControllerFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getControllerClassName(settings);
  }

  public static String getConfigurationFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getConfigurationClassName(settings);
  }

  private static String getPackage(Settings settings, String... subpackages) {
    String base = settings.getBasePackage();
    for (String subpackage : subpackages) {
      base += '.' + subpackage;
    }
    return base;
  }

}
