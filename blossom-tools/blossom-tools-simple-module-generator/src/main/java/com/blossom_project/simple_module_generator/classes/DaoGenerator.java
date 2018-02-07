package com.blossom_project.simple_module_generator.classes;

import com.helger.jcodemodel.EClassType;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.blossom_project.core.common.dao.CrudDao;
import com.blossom_project.simple_module_generator.GeneratorUtils;
import com.blossom_project.simple_module_generator.Parameters;

public class DaoGenerator implements ClassGenerator {
  private JDefinedClass entityClass;

  public DaoGenerator(JDefinedClass entityClass) {
    this.entityClass= entityClass;
  }

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel._class(GeneratorUtils.getDaoFullyQualifiedClassName(parameters),
        EClassType.INTERFACE);
      definedClass._extends(codeModel.ref(CrudDao.class).narrow(entityClass));

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

}
