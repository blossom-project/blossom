package fr.mgargadennec.blossom.simple_module_generator.classes;

import com.helger.jcodemodel.EClassType;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import fr.mgargadennec.blossom.core.common.service.CrudService;
import fr.mgargadennec.blossom.simple_module_generator.GeneratorUtils;
import fr.mgargadennec.blossom.simple_module_generator.Parameters;

public class ServiceGenerator implements ClassGenerator {

  private final JDefinedClass dtoClass;
  private final JDefinedClass createFormClass;
  private final JDefinedClass updateFormClass;

  public ServiceGenerator(JDefinedClass dtoClass, JDefinedClass createFormClass,
    JDefinedClass updateFormClass) {
    this.dtoClass = dtoClass;
    this.createFormClass = createFormClass;
    this.updateFormClass = updateFormClass;
  }

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getServiceFullyQualifiedClassName(parameters),
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
