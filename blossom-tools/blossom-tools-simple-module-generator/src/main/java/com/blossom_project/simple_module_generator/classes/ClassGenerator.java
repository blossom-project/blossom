package com.blossom_project.simple_module_generator.classes;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.blossom_project.simple_module_generator.Parameters;

public interface ClassGenerator {

  JDefinedClass generate(Parameters parameters, JCodeModel codeModel);

}
