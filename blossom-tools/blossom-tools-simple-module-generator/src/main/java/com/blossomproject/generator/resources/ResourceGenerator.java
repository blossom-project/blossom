package com.blossomproject.generator.resources;

import com.blossomproject.generator.configuration.model.Settings;
import java.util.Map;

public interface ResourceGenerator {

  void prepare(Settings settings);

  void generate(Settings settings, Map<String, String> params);

}
