package fr.blossom.generator.resources;

import fr.blossom.generator.configuration.model.Settings;
import java.util.Map;

public interface ResourceGenerator {

  void prepare(Settings settings);

  void generate(Settings settings, Map<String, String> params);

}
