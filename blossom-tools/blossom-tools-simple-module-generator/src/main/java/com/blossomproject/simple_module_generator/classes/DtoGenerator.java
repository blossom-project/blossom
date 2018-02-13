package com.blossomproject.simple_module_generator.classes;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossomproject.core.common.dto.AbstractDTO;
import com.blossomproject.simple_module_generator.EntityField;
import com.blossomproject.simple_module_generator.GeneratorUtils;
import com.blossomproject.simple_module_generator.Parameters;

public class DtoGenerator implements ClassGenerator {

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel._class(GeneratorUtils.getDtoFullyQualifiedClassName(parameters));
      definedClass._extends(AbstractDTO.class);

      // Fields
      for (EntityField field : parameters.getFields()) {
        addField(codeModel, definedClass, field);
      }

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate service DTO class", e);
    }
  }

  private void addField(JCodeModel codeModel, JDefinedClass definedClass, EntityField field) {
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
