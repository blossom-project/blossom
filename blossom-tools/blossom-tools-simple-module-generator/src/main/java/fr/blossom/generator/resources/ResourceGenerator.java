package fr.blossom.generator.resources;

import fr.blossom.generator.configuration.model.Settings;
import java.util.Map;

public interface ResourceGenerator {

  void generate(Settings settings, Map<String, String> params);
}
