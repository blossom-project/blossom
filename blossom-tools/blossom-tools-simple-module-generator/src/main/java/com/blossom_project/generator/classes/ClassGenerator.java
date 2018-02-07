package com.blossom_project.generator.classes;

import com.helger.jcodemodel.JCodeModel;
import com.helger.jcodemodel.JDefinedClass;
import com.blossom_project.generator.configuration.model.Settings;

public interface ClassGenerator {

  void prepare(Settings settings, JCodeModel codeModel);

  JDefinedClass generate(Settings settings, JCodeModel codeModel);

}
