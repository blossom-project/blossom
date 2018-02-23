package com.blossomproject.generator.resources;

import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.Settings;
import com.blossomproject.generator.utils.GeneratorUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

public class EntityInformationsViewGenerator implements ResourceGenerator {

  @Override
  public void prepare(Settings settings) {

  }

  @Override
  public void generate(Settings settings, Map<String, String> params) {
    try {
      URL url = Resources.getResource("entityinformations.ftl");
      String content = Resources.toString(url, Charsets.UTF_8);

      for (Entry<String, String> entry : params.entrySet()) {
        content = content.replaceAll("%%" + entry.getKey() + "%%", entry.getValue());
      }

      content = GeneratorUtils.generateFormFields(content, settings.getFields());

      Path templateRoot = settings.getResourcePath().resolve("templates").resolve("modules")
        .resolve(settings.getEntityNameLowerUnderscore() + "s");
      Files.createDirectories(templateRoot);

      Files.write(templateRoot.resolve(settings.getEntityNameLowerUnderscore() + "informations.ftl"),
        content.getBytes(), StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
