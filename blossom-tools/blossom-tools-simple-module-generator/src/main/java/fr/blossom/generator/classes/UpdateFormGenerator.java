package fr.blossom.generator.classes;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JExpr;
import com.helger.jcodemodel.JFieldVar;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.Settings;
import fr.blossom.generator.configuration.model.StringField;
import fr.blossom.generator.utils.GeneratorUtils;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.NotBlank;

public class UpdateFormGenerator implements ClassGenerator {

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {

  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getUpdateFormFullyQualifiedClassName(settings));

      // Fields
      for (Field field : settings.getFields()) {
        if (field.isPossibleUpdate()) {
          addField(settings, codeModel, definedClass, field);
        }
      }

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate service DTO class", e);
    }
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
