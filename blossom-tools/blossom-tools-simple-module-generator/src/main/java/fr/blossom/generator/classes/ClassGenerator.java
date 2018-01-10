package fr.blossom.generator.classes;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import fr.blossom.generator.configuration.model.Settings;

public interface ClassGenerator {

  void prepare(Settings settings, JCodeModel codeModel);

  JDefinedClass generate(Settings settings, JCodeModel codeModel);

}
