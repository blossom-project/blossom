package fr.mgargadennec.blossom.simple_module_generator.classes;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import fr.mgargadennec.blossom.simple_module_generator.EntityField;
import fr.mgargadennec.blossom.simple_module_generator.GeneratorUtils;
import fr.mgargadennec.blossom.simple_module_generator.Parameters;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotBlank;

public class UpdateFormGenerator implements ClassGenerator {

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel._class(GeneratorUtils.getUpdateFormFullyQualifiedClassName(parameters));

      // Fields
      for (EntityField field : parameters.getFields()) {
        if(field.isPossibleUpdate()) {
          addField(parameters, codeModel, definedClass, field);
        }
      }

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate service DTO class", e);
    }
  }

  private void addField(Parameters parameters,JCodeModel codeModel, JDefinedClass definedClass, EntityField field) {
    // Field
    JFieldVar fieldVar = definedClass.field(JMod.PRIVATE, codeModel.ref(field.getClassName()), field.getName());

    if(!field.isNullable()){
      String message = "{"+parameters.getEntityNameLowerUnderscore()+"s."+parameters.getEntityNameLowerUnderscore()+".validation."+field.getName()+".NotNull.message"+"}";
      fieldVar.annotate(NotNull.class).param("message", message);
    }
    if(!field.isNotBlank()){
      String message = "{"+parameters.getEntityNameLowerUnderscore()+"s."+parameters.getEntityNameLowerUnderscore()+".validation."+field.getName()+".NotBlank.message"+"}";
      fieldVar.annotate(NotBlank.class).param("message", message);
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
