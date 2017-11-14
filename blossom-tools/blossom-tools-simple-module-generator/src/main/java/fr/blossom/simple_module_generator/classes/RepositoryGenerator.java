package fr.blossom.simple_module_generator.classes;

import com.helger.jcodemodel.EClassType;
import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import fr.blossom.core.common.repository.CrudRepository;
import fr.blossom.simple_module_generator.GeneratorUtils;
import fr.blossom.simple_module_generator.Parameters;
import org.springframework.stereotype.Repository;

public class RepositoryGenerator implements ClassGenerator {

  private JDefinedClass poClass;

  public RepositoryGenerator(JDefinedClass poClass) {
    this.poClass = poClass;
  }

  public JDefinedClass generate(Parameters parameters, JCodeModel codeModel) {
    try {

      JDefinedClass definedClass = codeModel
        ._class(GeneratorUtils.getRepositoryFullyQualifiedClassName(parameters),
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
