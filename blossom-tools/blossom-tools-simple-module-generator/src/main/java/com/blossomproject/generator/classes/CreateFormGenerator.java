package com.blossomproject.generator.classes;

import com.blossomproject.generator.configuration.model.TemporalField;
import com.blossomproject.generator.configuration.model.impl.DefaultField;
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
          GeneratorUtils.addField(settings, codeModel, definedClass, field);
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



}
