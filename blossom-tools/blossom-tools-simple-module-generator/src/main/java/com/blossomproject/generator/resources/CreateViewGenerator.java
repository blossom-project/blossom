package com.blossomproject.generator.resources;

import com.blossomproject.generator.configuration.model.Field;
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

      params.put("CREATE_FORM", settings.getEntityNameLowerCamel()+"CreateForm");

      URL url = Resources.getResource("create.ftl");
      String content = Resources.toString(url, Charsets.UTF_8);

      for(Entry<String,String> entry : params.entrySet()){
        content = content.replaceAll("%%"+entry.getKey()+"%%", entry.getValue());
      }

      content = generateFormFields(content, settings);

      Path templateRoot = settings.getResourcePath().resolve("templates").resolve("modules").resolve(settings.getEntityNameLowerUnderscore()+"s");
      Files.createDirectories(templateRoot);

      Files.write(templateRoot.resolve(settings.getEntityNameLowerUnderscore()+"_create.ftl"), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

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
      String formField = formFieldTemplate.replaceAll("%%FIELD_NAME%%", field.getName()).replaceAll("%%FIELD_LABEL%%", field.getName());
      form+=formField;
    }
    String test = content.substring(0,startTagPosition);
    String test2 = content.substring(endTagPosition);
    return content.substring(0,startTagPosition)+form+content.substring((endTagPosition + formTag.length()+5));
  }
}
