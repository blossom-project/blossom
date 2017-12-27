package fr.blossom.simple_module_generator.resources;

import fr.blossom.simple_module_generator.Parameters;
import java.nio.file.Path;
import java.util.Map;

public interface ResourceGenerator {

  void generate(Path outputPath, Parameters parameters,Map<String,String> params);
}
