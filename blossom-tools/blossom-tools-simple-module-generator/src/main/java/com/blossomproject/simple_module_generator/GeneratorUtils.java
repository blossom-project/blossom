package com.blossomproject.simple_module_generator;

public class GeneratorUtils {

  public static String getEntityClassName(Parameters parameters) {
    return parameters.getEntityName();
  }

  public static String getRepositoryClassName(Parameters parameters) {
    return parameters.getEntityName() + "Repository";
  }

  public static String getDaoClassName(Parameters parameters) {
    return  parameters.getEntityName() + "Dao";
  }

  public static String getDaoImplClassName(Parameters parameters) {
    return parameters.getEntityName() + "DaoImpl";
  }

  public static String getServiceClassName(Parameters parameters) {
    return  parameters.getEntityName() + "Service";
  }

  public static String getServiceImplClassName(Parameters parameters) {
    return  parameters.getEntityName() + "ServiceImpl";
  }

  public static String getDtoClassName(Parameters parameters) {
    return  parameters.getEntityName() + "DTO";
  }

  public static String getMapperClassName(Parameters parameters) {
    return  parameters.getEntityName() + "DTOMapper";
  }

  public static String getCreateFormClassName(Parameters parameters) {
    return  parameters.getEntityName() + "CreateForm";
  }

  public static String getUpdateFormClassName(Parameters parameters) {
    return  parameters.getEntityName() + "UpdateForm";
  }

  public static String getControllerClassName(Parameters parameters) {
    return  parameters.getEntityName() + "Controller";
  }

  public static String getConfigurationClassName(Parameters parameters) {
    return  parameters.getEntityName() + "Configuration";
  }

  public static String getEntityFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getEntityClassName(parameters);
  }

  public static String getRepositoryFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getRepositoryClassName(parameters);
  }

  public static String getDaoFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getDaoClassName(parameters);
  }

  public static String getDaoImplFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getDaoImplClassName(parameters);
  }

  public static String getServiceFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getServiceClassName(parameters);
  }

  public static String getServiceImplFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getServiceImplClassName(parameters);
  }

  public static String getDtoFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getDtoClassName(parameters);
  }

  public static String getCreateFormFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getCreateFormClassName(parameters);
  }

  public static String getUpdateFormFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getUpdateFormClassName(parameters);
  }

  public static String getMapperFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getMapperClassName(parameters);
  }

  public static String getControllerFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getControllerClassName(parameters);
  }

  public static String getConfigurationFullyQualifiedClassName(Parameters parameters) {
    return getPackage(parameters) + '.' + getConfigurationClassName(parameters);
  }

  private static String getPackage(Parameters parameters, String... subpackages) {
    String base = parameters.getBasePackage();
    for (String subpackage : subpackages) {
      base += '.' + subpackage;
    }
    return base;
  }

}
