package com.blossomproject.simple_module_generator.classes;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.blossomproject.simple_module_generator.Parameters;

public interface ClassGenerator {

  JDefinedClass generate(Parameters parameters, JCodeModel codeModel);

}
