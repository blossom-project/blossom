package com.blossomproject.generator.classes;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.EClassType;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.blossomproject.core.common.dao.CrudDao;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.utils.GeneratorUtils;

public class DaoGenerator implements ClassGenerator {

  private AbstractJClass entityClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.entityClass = codeModel.ref(GeneratorUtils.getEntityFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getDaoFullyQualifiedClassName(settings),
          EClassType.INTERFACE);
      definedClass._extends(codeModel.ref(CrudDao.class).narrow(entityClass));

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

}
