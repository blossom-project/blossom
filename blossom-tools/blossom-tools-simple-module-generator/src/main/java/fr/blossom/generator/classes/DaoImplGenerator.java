package fr.blossom.generator.classes;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.JBlock;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.helger.jcodemodel.JMethod;
import com.helger.jcodemodel.JMod;
import com.helger.jcodemodel.JVar;
import fr.blossom.core.common.dao.GenericCrudDaoImpl;
import fr.blossom.generator.configuration.model.Field;
import fr.blossom.generator.configuration.model.Settings;
import fr.blossom.generator.utils.GeneratorUtils;

public class DaoImplGenerator implements ClassGenerator {

  private AbstractJClass entityClass;
  private AbstractJClass daoClass;
  private AbstractJClass repositoryClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.entityClass = codeModel.ref(GeneratorUtils.getEntityFullyQualifiedClassName(settings));
    this.repositoryClass = codeModel.ref(GeneratorUtils.getRepositoryFullyQualifiedClassName(settings));
    this.daoClass = codeModel.ref(GeneratorUtils.getDaoFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel._class(GeneratorUtils.getDaoImplFullyQualifiedClassName(settings));
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
      for (Field field : settings.getFields()) {
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
