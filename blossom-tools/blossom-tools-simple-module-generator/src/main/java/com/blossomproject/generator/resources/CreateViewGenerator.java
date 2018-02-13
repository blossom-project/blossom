package com.blossomproject.generator.resources;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.blossomproject.generator.configuration.model.Settings;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;

public class CreateViewGenerator implements ResourceGenerator {

  @Override
  public void prepare(Settings settings) {

  }

  @Override
  public void generate(Settings settings, Map<String, String> params) {
    try {
      params.put("ENTITY_COLUMNS", "\"name\": { \"label\":\""+settings.getEntityNameLowerUnderscore()+"s"+"."+settings.getEntityNameLowerUnderscore()+".properties.name\", \"sortable\":true, \"link\":\""+params.get("LINK_ITEM")+"\"},\n"
        + "  \"modificationDate\": {\"label\":\"list.modification.date.head\", \"sortable\":true, \"type\":\"datetime\"}");

      URL url = Resources.getResource("create.ftl");
      String content = Resources.toString(url, Charsets.UTF_8);

      for(Entry<String,String> entry : params.entrySet()){
        content = content.replaceAll("%%"+entry.getKey()+"%%", entry.getValue());
      }

      Path templateRoot = settings.getResourcePath().resolve("templates").resolve("modules").resolve(settings.getEntityNameLowerUnderscore()+"s");
      Files.createDirectories(templateRoot);

      Files.write(templateRoot.resolve(settings.getEntityNameLowerUnderscore()+"_create.ftl"), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
