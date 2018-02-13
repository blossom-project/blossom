package com.blossomproject.simple_module_generator.resources;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.blossomproject.simple_module_generator.Parameters;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;

public class ListViewGenerator implements ResourceGenerator{

  @Override
  public void generate(Path path, Parameters parameters, Map<String, String> params) {
    try {
      params.put("ENTITY_COLUMNS", "\"name\": { \"label\":\""+parameters.getEntityNameLowerUnderscore()+"s"+"."+parameters.getEntityNameLowerUnderscore()+".properties.name\", \"sortable\":true, \"link\":\""+params.get("LINK_ITEM")+"\"},\n"
        + "  \"modificationDate\": {\"label\":\"list.modification.date.head\", \"sortable\":true, \"type\":\"datetime\"}");

      URL url = Resources.getResource("list.ftl");
      String content = Resources.toString(url, Charsets.UTF_8);

      for(Entry<String,String> entry : params.entrySet()){
        content = content.replaceAll("%%"+entry.getKey()+"%%", entry.getValue());
      }

      Files.write(path, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

      System.out.println(content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
