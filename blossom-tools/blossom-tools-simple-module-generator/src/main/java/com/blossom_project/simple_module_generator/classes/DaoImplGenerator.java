package com.blossom_project.simple_module_generator.classes;

import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import com.blossom_project.core.common.dao.GenericCrudDaoImpl;
import com.blossom_project.simple_module_generator.EntityField;
import com.blossom_project.simple_module_generator.GeneratorUtils;
import com.blossom_project.simple_module_generator.Parameters;

public class DaoImplGenerator implements ClassGenerator {

  private JDefinedClass entityClass;
  private JDefinedClass daoClass;
  private JDefinedClass repositoryClass;

  public DaoImplGenerator(JDefinedClass entityClass, JDefinedClass repositoryClass,
    JDefinedClass daoClass) {
    this.entityClass = entityClass;
    this.repositoryClass = repositoryClass;
    this.daoClass = daoClass;
  }

  @Override
  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getDaoImplFullyQualifiedClassName(parameters));
      definedClass._extends(codeModel.ref(GenericCrudDaoImpl.class).narrow(entityClass));
      definedClass._implements(daoClass);

      JMethod constructor = definedClass.constructor(JMod.PUBLIC);
      JVar repositoryParam = constructor.param(repositoryClass, "repository");

      JBlock constructorBody = constructor.body();
      constructorBody.invoke("super").arg(repositoryParam);

      JMethod updateEntity = definedClass.method(JMod.PUBLIC, entityClass, "updateEntity");
      updateEntity.annotate(Override.class);
      JVar originalEntity = updateEntity.param(entityClass, "originalEntity");
      JVar modifiedEntity = updateEntity.param(entityClass, "modifiedEntity");

      JBlock updateEntityBody = updateEntity.body();
      for (EntityField field : parameters.getFields()) {
        if (field.isPossibleUpdate()) {
          updateEntityBody.add(originalEntity.invoke(field.getSetterName()).arg(modifiedEntity.invoke(field.getGetterName())));
        }
      }

      updateEntityBody._return(originalEntity);

      return definedClass;
    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

}
