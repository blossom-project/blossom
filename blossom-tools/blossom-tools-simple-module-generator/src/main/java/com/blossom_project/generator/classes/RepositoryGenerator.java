package com.blossom_project.generator.classes;

import com.helger.jcodemodel.AbstractJClass;
import com.helger.jcodemodel.EClassType;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.blossom_project.core.common.repository.CrudRepository;
import com.blossom_project.generator.configuration.model.Settings;
import com.blossom_project.generator.utils.GeneratorUtils;
import org.springframework.stereotype.Repository;

public class RepositoryGenerator implements ClassGenerator {

  private AbstractJClass poClass;

  @Override
  public void prepare(Settings settings, JCodeModel codeModel) {
    this.poClass = codeModel.ref(GeneratorUtils.getEntityFullyQualifiedClassName(settings));
  }

  @Override
  public JDefinedClass generate(Settings settings, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getRepositoryFullyQualifiedClassName(settings),
          EClassType.INTERFACE);
      definedClass._extends(codeModel.ref(CrudRepository.class).narrow(poClass));
      definedClass.annotate(Repository.class);

      return definedClass;

    } catch (Exception e) {
      e.printStackTrace();
      throw new RuntimeException("Can't generate repository class", e);
    }
  }

}
