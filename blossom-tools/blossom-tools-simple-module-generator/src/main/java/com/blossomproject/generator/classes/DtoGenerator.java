package com.blossomproject.generator.classes;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.utils.GeneratorUtils;
import com.blossomproject.generator.configuration.model.Settings;

public class DtoGenerator implements ClassGenerator {

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {

  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel._class(GeneratorUtils.getDtoFullyQualifiedClassName(settings));
      definedClass._extends(AbstractDTO.class);

      // Fields
      for (Field field : settings.getFields()) {
        addField(codeModel, definedClass, field);
      }

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate service DTO class", e);
    }
  }

  private void addField(JCodeModel codeModel, JDefinedClass definedClass, Field field) {
    // Field
    JFieldVar fieldVar = definedClass.field(JMod.PRIVATE, codeModel.ref(field.getClassName()), field.getName());

    // Getter
    JMethod getter = definedClass.method(JMod.PUBLIC, fieldVar.type(), field.getGetterName());
    getter.body()._return(fieldVar);

    // Setter
    JMethod setter = definedClass.method(JMod.PUBLIC, void.class, field.getSetterName());
    JVar param = setter.param(fieldVar.type(), field.getName());
    setter.body().assign(JExpr.refthis(fieldVar.name()), param);
  }

}
