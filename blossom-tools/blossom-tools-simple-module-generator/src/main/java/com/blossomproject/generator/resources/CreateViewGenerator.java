package com.blossomproject.generator.resources;

import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.utils.GeneratorUtils;
import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.blossomproject.generator.configuration.model.Settings;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.sql.Timestamp;
import java.util.Map;
import java.util.Map.Entry;

public class CreateViewGenerator implements ResourceGenerator {

  @Override
  public void prepare(Settings settings) {

  }

  @Override
  public void generate(Settings settings, Map<String, String> params) {
    try {
      params.put("CREATE_FORM", settings.getEntityNameLowerCamel()+"CreateForm");

      URL url = Resources.getResource("create.ftl");
      String content = Resources.toString(url, Charsets.UTF_8);

      for(Entry<String,String> entry : params.entrySet()){
        content = content.replaceAll("%%"+entry.getKey()+"%%", entry.getValue());
      }

      content = GeneratorUtils.generateFormFields(content, settings);

      Path templateRoot = settings.getResourcePath().resolve("templates").resolve("modules").resolve(settings.getEntityNameLowerUnderscore()+"s");
      Files.createDirectories(templateRoot);

      Files.write(templateRoot.resolve("create.ftl"), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
