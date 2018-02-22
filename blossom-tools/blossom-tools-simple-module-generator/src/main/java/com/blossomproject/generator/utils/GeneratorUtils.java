package com.blossomproject.generator.utils;

import com.blossomproject.generator.configuration.model.Field;
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

  public static String generateFormFields (String content, Settings settings){
    String formTag = "FIELD_FORM";
    int startTagPosition = content.indexOf("%%"+formTag+"%%");
    int endTagPosition = content.indexOf("%%/"+formTag+"%%", startTagPosition);
    String formFieldTemplate = content.substring(startTagPosition+formTag.length()+4, endTagPosition);

    String formTagInput = "FIELD_FORM_INPUT";
    int startTagPositionInput = formFieldTemplate.indexOf("%%"+formTagInput+"%%");
    int endTagPositionInput = formFieldTemplate.indexOf("%%/"+formTagInput+"%%", startTagPositionInput);
    String formFieldTemplateInput = formFieldTemplate.substring(startTagPositionInput+formTagInput.length()+4, endTagPositionInput);

    String formTagBoolean = "FIELD_FORM_BOOLEAN";
    int startTagPositionBoolean = formFieldTemplate.indexOf("%%"+formTagBoolean+"%%");
    int endTagPositionBoolean = formFieldTemplate.indexOf("%%/"+formTagBoolean+"%%", startTagPositionBoolean);
    String formFieldTemplateBoolean = formFieldTemplate.substring(startTagPositionBoolean+formTagBoolean.length()+4, endTagPositionBoolean);

    String form = "";
    for(Field field : settings.getFields()){
      String formField = "";
      if(field.isRequiredCreate()){
        if ("boolean".equals(field.getJdbcType())){
          formField = formFieldTemplateBoolean.replaceAll("%%FIELD_NAME%%", field.getName()).replaceAll("%%FIELD_LABEL%%", field.getName());
        }
        else {
          String htmlType = getHtmlType(field);
          String htmlCast = getHtmlCast(field);
          formField = formFieldTemplateInput.replaceAll("%%FIELD_NAME%%", field.getName()).replaceAll("%%FIELD_LABEL%%", field.getName()).replaceAll("%%FIELD_TYPE%%", htmlType).replaceAll("%%FIELD_CAST%%", htmlCast);
        }
        form+=formField;
      }
    }
    return content.substring(0,startTagPosition)+form+content.substring((endTagPosition + formTag.length()+5));
  }

  public static String getHtmlType(Field field){
    String type = field.getJdbcType();
    if("date".equals(type)){
      return "date";
    }
    if("timestamp".equals(type)){
      return "datetime-local";
    }
    if("time".equals(type)){
      return "time";
    }
    if("integer".equals(type)){
      return "number";
    }
    return "text";
  }

  public static String getHtmlCast(Field field){
    String type = field.getJdbcType();
    if("date".equals(type)){
      return "?string(\"yyyy-MM-dd\")";
    }
    if("timestamp".equals(type)){
      return "?string(\"yyyy-MM-dd'T'HH:mm\")";
    }
    if("time".equals(type)){
      return "?time";
    }
    return "";
  }

}
