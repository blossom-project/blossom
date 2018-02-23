package com.blossomproject.generator.classes;

import com.blossomproject.core.common.search.SearchEngineImpl;
import com.blossomproject.generator.configuration.model.TemporalField;
import com.helger.jcodemodel.*;
import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.configuration.model.StringField;
import com.blossomproject.generator.utils.GeneratorUtils;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;

public class UpdateFormGenerator implements ClassGenerator {

  private AbstractJClass dtoClass;
  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getUpdateFormFullyQualifiedClassName(settings));

      JMethod constructor = definedClass.constructor(JMod.PUBLIC);
      JVar entityDTO = constructor.param(dtoClass, "entityDTO");
      // Fields
      for (Field field : settings.getFields()) {
        if (field.isPossibleUpdate()) {
          JFieldVar fieldVar = GeneratorUtils.addField(settings, codeModel, definedClass, field);
          constructor.body().assign(JExpr.refthis(fieldVar.name()), entityDTO.invoke(field.getGetterName()));
        }
      }

      JMethod constructorEmpty = definedClass.constructor(JMod.PUBLIC);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate service DTO class", e);
    }
  }


}
