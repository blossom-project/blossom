package com.blossomproject.generator.utils;

import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.configuration.model.StringField;
import com.blossomproject.generator.configuration.model.TemporalField;
import com.blossomproject.generator.configuration.model.impl.EnumField;
import com.helger.jcodemodel.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

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


  public static String getIndexationJobClassName(Settings settings) {
    return settings.getEntityName() + "IndexationJob";
  }

  public static String getApiControllerClassName(Settings settings) {
    return  settings.getEntityName() + "ApiController";
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

  public static String getApiControllerFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getApiControllerClassName(settings);
  }

  public static String getConfigurationFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getConfigurationClassName(settings);
  }

  public static String getIndexationJobFullyQualifiedClassName(Settings settings) {
    return getPackage(settings) + '.' + getIndexationJobClassName(settings);
  }

  private static String getPackage(Settings settings, String... subpackages) {
    String base = settings.getBasePackage();
    for (String subpackage : subpackages) {
      base += '.' + subpackage;
    }
    return base;
  }

  public static String generateFormFields (String content, List<Field> fields){
    String formTag = "FIELD_FORM";
    int startTagPosition = content.indexOf("%%"+formTag+"%%");
    int endTagPosition = content.indexOf("%%/"+formTag+"%%", startTagPosition);

    String formFieldTemplate = content.substring(startTagPosition+formTag.length()+4, endTagPosition);

    String formFieldTemplateInput = getFormFieldTemplateInput(formFieldTemplate);

    String formFieldTemplateBoolean = getFormFieldTemplateBoolean(formFieldTemplate);

    String formFieldTemplateSelect = getFormFieldTemplateSelect(formFieldTemplate);

    String formFieldTemplateTextarea = getFormFieldTemplateTextarea(formFieldTemplate);

    String form = "";
    for(Field field : fields){
      form+= generateFormField(field, formFieldTemplateInput, formFieldTemplateBoolean, formFieldTemplateSelect, formFieldTemplateTextarea);
    }
    return content.substring(0,startTagPosition)+form+content.substring((endTagPosition + formTag.length()+5));
  }

  private static String getFormFieldTemplateInput(String formFieldTemplate){
    String formTagInput = "FIELD_FORM_INPUT";
    int startTagPositionInput = formFieldTemplate.indexOf("%%"+formTagInput+"%%");
    int endTagPositionInput = formFieldTemplate.indexOf("%%/"+formTagInput+"%%", startTagPositionInput);

    return formFieldTemplate.substring(startTagPositionInput+formTagInput.length()+4, endTagPositionInput);
  }

  private static String getFormFieldTemplateBoolean(String formFieldTemplate){
    String formTagBoolean = "FIELD_FORM_BOOLEAN";
    int startTagPositionBoolean = formFieldTemplate.indexOf("%%"+formTagBoolean+"%%");
    int endTagPositionBoolean = formFieldTemplate.indexOf("%%/"+formTagBoolean+"%%", startTagPositionBoolean);

    return formFieldTemplate.substring(startTagPositionBoolean+formTagBoolean.length()+4, endTagPositionBoolean);
  }

  private static String getFormFieldTemplateSelect(String formFieldTemplate){
    String formTagSelect = "FIELD_FORM_SELECT";
    int startTagPositionSelect = formFieldTemplate.indexOf("%%"+formTagSelect+"%%");
    int endTagPositionSelect = formFieldTemplate.indexOf("%%/"+formTagSelect+"%%", startTagPositionSelect);

    return formFieldTemplate.substring(startTagPositionSelect+formTagSelect.length()+4, endTagPositionSelect);
  }

  private static String getFormFieldTemplateTextarea(String formFieldTemplate){
    String formTagSelect = "FIELD_FORM_TEXTAREA";
    int startTagPositionTextarea= formFieldTemplate.indexOf("%%"+formTagSelect+"%%");
    int endTagPositionTextarea = formFieldTemplate.indexOf("%%/"+formTagSelect+"%%", startTagPositionTextarea);

    return formFieldTemplate.substring(startTagPositionTextarea+formTagSelect.length()+4, endTagPositionTextarea);
  }

  private static String generateFormField(Field field, String formFieldTemplateInput, String formFieldTemplateBoolean, String formFieldTemplateSelect, String formFieldTemplateTextarea){
    String formField;
    if ("boolean".equals(field.getJdbcType())){
      formField = generateFormFieldBoolean(field, formFieldTemplateBoolean);
    }
    else if(field instanceof EnumField){
      formField = generateFormFieldSelect(field, formFieldTemplateSelect);
    }
    else if(field instanceof StringField && ((StringField) field).isLob()) {
      formField = generateFormFieldTextarea(field, formFieldTemplateTextarea);
    }
    else {
      String htmlType = getHtmlType(field);
      String htmlCast = getHtmlCast(field);
      formField = generateFormFieldInput(field, formFieldTemplateInput, htmlType, htmlCast);
    }
    return formField;
  }

  private static String generateFormFieldBoolean(Field field, String formFieldTemplateBoolean){
    return formFieldTemplateBoolean.replaceAll("%%FIELD_NAME%%", field.getName()).replaceAll("%%FIELD_LABEL%%", field.getName());
  }

  private static String generateFormFieldSelect(Field field, String formFieldTemplateSelect){
    String formTagOption = "FIELD_FORM_SELECT_OPTION";
    int startTagPositionOption = formFieldTemplateSelect.indexOf("%%"+formTagOption+"%%");
    int endTagPositionOption = formFieldTemplateSelect.indexOf("%%/"+formTagOption+"%%", startTagPositionOption);
    String formFieldTemplateOption = formFieldTemplateSelect.substring(startTagPositionOption+formTagOption.length()+4, endTagPositionOption);

    String options = "";
    if(field.isNullable()){
      options += formFieldTemplateOption.replaceAll("%%OPTION_VALUE%%", "").replaceAll("%%OPTION_LABEL%%", "");
    }
    for(Object enumObj : EnumSet.allOf((Class<? extends Enum>)field.getClassName()) ){
      Enum enumCasted =  (Enum) enumObj;
      options += formFieldTemplateOption.replaceAll("%%OPTION_VALUE%%", enumCasted.toString()).replaceAll("%%OPTION_LABEL%%", enumCasted.toString());
    }
    String formFieldTemplateSelectReplaced = formFieldTemplateSelect.substring(0,startTagPositionOption)+options+formFieldTemplateSelect.substring((endTagPositionOption + formTagOption.length()+5));
    return formFieldTemplateSelectReplaced.replace("%%FIELD_NAME%%", field.getName()).replace("%%FIELD_LABEL%%", field.getName());
  }

  private static String generateFormFieldInput(Field field, String formFieldTemplateInput, String htmlType, String htmlCast){
    return formFieldTemplateInput.replaceAll("%%FIELD_NAME%%", field.getName()).replaceAll("%%FIELD_LABEL%%", field.getName()).replaceAll("%%FIELD_TYPE%%", htmlType).replaceAll("%%FIELD_CAST%%", htmlCast);
  }

  private static String generateFormFieldTextarea(Field field, String formFieldTemplateTextarea){
    return formFieldTemplateTextarea.replaceAll("%%FIELD_NAME%%", field.getName()).replaceAll("%%FIELD_LABEL%%", field.getName());
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
      if(!Timestamp.class.equals(field.getClassName())){
        return "?string(\"yyyy-MM-dd'T'HH:mm\")";
      }
    }
    if("time".equals(type)){
      return "?string(\"HH:mm\")";
    }
    return "";
  }


  public static JFieldVar addField(Settings settings, JCodeModel codeModel, JDefinedClass definedClass,
                             Field field) {
    // Field
    JFieldVar fieldVar = addFieldDefinition(settings, field, codeModel, definedClass);

    addFieldAnnotations(settings, field, fieldVar, codeModel);

    // Getter
    JMethod getter = definedClass.method(JMod.PUBLIC, fieldVar.type(), field.getGetterName());
    getter.body()._return(fieldVar);

    // Setter
    JMethod setter = definedClass.method(JMod.PUBLIC, void.class, field.getSetterName());
    JVar param = setter.param(fieldVar.type(), field.getName());
    setter.body().assign(JExpr.refthis(fieldVar.name()), param);

    return fieldVar;
  }

  private static JFieldVar addFieldDefinition(Settings settings, Field field,JCodeModel codeModel, JDefinedClass definedClass){
    JFieldVar fieldVar;
    if("boolean".equals(field.getJdbcType())){
      fieldVar = definedClass
              .field(JMod.PRIVATE, codeModel.ref(field.getClassName()), field.getName(), JExpr.lit(false));
    }
    else {
      fieldVar = definedClass
              .field(JMod.PRIVATE, codeModel.ref(field.getClassName()), field.getName());
    }
    if (!field.isNullable()) {
      String message = "{" + settings.getEntityNameLowerUnderscore() + "s." + settings
              .getEntityNameLowerUnderscore() + ".validation." + field.getName() + ".NotNull.message"
              + "}";
      fieldVar.annotate(NotNull.class).param("message", message);
    }
    return fieldVar;
  }

  private static void addFieldAnnotations(Settings settings, Field field, JFieldVar fieldVar, JCodeModel codeModel){
    if (field instanceof StringField) {
      if (!((StringField) field).isNotBlank()) {
        String message = "{" + settings.getEntityNameLowerUnderscore() + "s." + settings
                .getEntityNameLowerUnderscore() + ".validation." + field.getName() + ".NotBlank.message"
                + "}";
        fieldVar.annotate(NotBlank.class).param("message", message);
      }
      Integer stringMaxLength = ((StringField) field).getMaxLength();
      if(stringMaxLength != null){
        fieldVar.annotate(Size.class).param("max", stringMaxLength);
      }
    }
    else if(field instanceof TemporalField){
      TemporalField temporalField = (TemporalField) field;
      if(temporalField.getTemporalType()== TemporalType.TIME){
        fieldVar.annotate(DateTimeFormat.class).param("pattern", "HH:mm");
      }
      else if(temporalField.getTemporalType()== TemporalType.TIMESTAMP){
        fieldVar.annotate(DateTimeFormat.class).param("pattern", "yyyy-MM-dd'T'HH:mm");
      }
      else {
        fieldVar.annotate(DateTimeFormat.class).param("pattern", "yyyy-MM-dd");
      }
    }
    else if(field instanceof EnumField){
      fieldVar.type(codeModel.ref(settings.getBasePackage()+"."+settings.getEntityName()+"."+field.getClassName().getSimpleName()));
    }
  }

}
