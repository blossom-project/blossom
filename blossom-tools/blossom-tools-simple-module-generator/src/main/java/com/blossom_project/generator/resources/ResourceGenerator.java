package com.blossom_project.generator.resources;

import com.blossom_project.generator.configuration.model.Settings;
import java.util.Map;

public interface ResourceGenerator {

  void prepare(Settings settings);

  void generate(Settings settings, Map<String, String> params);

}
