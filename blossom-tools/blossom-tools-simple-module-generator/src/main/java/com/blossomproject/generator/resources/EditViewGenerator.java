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

public class EditViewGenerator implements ResourceGenerator {

  @Override
  public void prepare(Settings settings) {

  }

  @Override
  public void generate(Settings settings, Map<String, String> params) {
    try {
      params.put("UPDATE_FORM", settings.getEntityNameLowerCamel()+"UpdateForm");

      URL url = Resources.getResource("entityinformations-edit.ftl");
      String content = Resources.toString(url, Charsets.UTF_8);

      for(Entry<String,String> entry : params.entrySet()){
        content = content.replaceAll("%%"+entry.getKey()+"%%", entry.getValue());
      }

      List<Field> fieldsToGenerate = settings.getFields().stream().filter(f -> f.isPossibleUpdate()).collect(Collectors.toList());
      content = GeneratorUtils.generateFormFields(content, fieldsToGenerate);

      Path templateRoot = settings.getResourcePath().resolve("templates").resolve("modules").resolve(settings.getEntityNameLowerUnderscore()+"s");
      Files.createDirectories(templateRoot);

      Files.write(templateRoot.resolve(settings.getEntityNameLowerUnderscore() + "informations-edit.ftl"), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
