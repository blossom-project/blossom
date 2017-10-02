package fr.mgargadennec.blossom.simple_module_generator.views;

import fr.mgargadennec.blossom.simple_module_generator.Parameters;
import java.nio.file.Path;
import java.util.Map;

public interface ResourceGenerator {

  void generate(Path outputPath, Parameters parameters,Map<String,String> params);
}
