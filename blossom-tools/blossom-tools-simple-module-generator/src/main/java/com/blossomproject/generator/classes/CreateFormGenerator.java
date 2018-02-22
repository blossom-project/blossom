package com.blossomproject.generator.classes;

import com.blossomproject.generator.configuration.model.TemporalField;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.configuration.model.StringField;
import com.blossomproject.generator.utils.GeneratorUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Timestamp;

public class CreateFormGenerator implements ClassGenerator {

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {
      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getCreateFormFullyQualifiedClassName(settings));

      // Fields
      for (Field field : settings.getFields()) {
        if (field.isRequiredCreate()) {
          addField(settings, codeModel, definedClass, field);
        }
      }

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate service DTO class", e);
    }
  }

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {

  }

  private void addField(Settings settings, JCodeModel codeModel, JDefinedClass definedClass,
    Field field) {
    // Field
    JFieldVar fieldVar = definedClass
      .field(JMod.PRIVATE, codeModel.ref(field.getClassName()), field.getName());
    if (!field.isNullable()) {
      String message = "{" + settings.getEntityNameLowerUnderscore() + "s." + settings
        .getEntityNameLowerUnderscore() + ".validation." + field.getName() + ".NotNull.message"
        + "}";
      fieldVar.annotate(NotNull.class).param("message", message);
    }

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

    // Getter
    JMethod getter = definedClass.method(JMod.PUBLIC, fieldVar.type(), field.getGetterName());
    getter.body()._return(fieldVar);

    // Setter
    JMethod setter = definedClass.method(JMod.PUBLIC, void.class, field.getSetterName());
    JVar param = setter.param(fieldVar.type(), field.getName());
    setter.body().assign(JExpr.refthis(fieldVar.name()), param);
  }

}
