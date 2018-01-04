package fr.blossom.generator.classes;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.EClassType;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import fr.blossom.core.common.service.CrudService;
import fr.blossom.generator.configuration.model.Settings;
import fr.blossom.generator.utils.GeneratorUtils;

public class ServiceGenerator implements ClassGenerator {

  private  AbstractJClass dtoClass;
  private AbstractJClass createFormClass;
  private AbstractJClass updateFormClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.dtoClass = codeModel.ref(GeneratorUtils.getDtoFullyQualifiedClassName(settings));;
    this.createFormClass = codeModel.ref(GeneratorUtils.getCreateFormFullyQualifiedClassName(settings));;
    this.updateFormClass = codeModel.ref(GeneratorUtils.getUpdateFormFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getServiceFullyQualifiedClassName(settings),
          EClassType.INTERFACE);
      definedClass._extends(codeModel.ref(CrudService.class).narrow(dtoClass));

      JMethod create = definedClass.method(JMod.NONE, dtoClass, "create");
      create.param(createFormClass, "createForm");

      JMethod update = definedClass.method(JMod.NONE, dtoClass, "update");
      update.param(Long.class,"id");
      update.param(updateFormClass, "updateForm");

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

}
