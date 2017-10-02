package fr.mgargadennec.blossom.simple_module_generator;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;

public interface ClassGenerator {

  JDefinedClass generate(Parameters parameters, JCodeModel codeModel);

}
