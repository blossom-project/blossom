package com.blossom_project.simple_module_generator.resources;

import com.blossom_project.simple_module_generator.Parameters;
import java.nio.file.Path;
import java.util.Map;

public interface ResourceGenerator {

  void generate(Path outputPath, Parameters parameters,Map<String,String> params);
}
