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
import java.util.Map;
import java.util.Map.Entry;

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

      content = GeneratorUtils.generateFormFields(content, settings);

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

  public String generateFormFields (String content, Settings settings){
    String formTag = "FIELD_FORM";
    int startTagPosition = content.indexOf("%%"+formTag+"%%");
    int endTagPosition = content.indexOf("%%/"+formTag+"%%", startTagPosition);
    String formFieldTemplate = content.substring(startTagPosition+formTag.length()+4, endTagPosition);

    String form = "";
    for(Field field : settings.getFields()){
      String formField = formFieldTemplate.replaceAll("%%FIELD_NAME%%", field.getName());
      form+=formField;
    }
    return content.substring(0,startTagPosition)+form+content.substring((endTagPosition + formTag.length()+5));
  }
}
