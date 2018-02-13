package com.blossomproject.simple_module_generator.resources;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.blossomproject.simple_module_generator.EntityField;
import com.blossomproject.simple_module_generator.Parameters;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;

public class ChangelogGenerator implements ResourceGenerator {

  @Override
  public void generate(Path path, Parameters parameters, Map<String, String> params) {
    try {
      URL url = Resources.getResource("changelog.xml");
      String content = Resources.toString(url, Charsets.UTF_8);

      Map<String, String> customParams = buildCustomParameters(parameters, params);

      for (Entry<String, String> entry : customParams.entrySet()) {
        content = content.replaceAll("%%" + entry.getKey() + "%%", entry.getValue());
      }

      Files.write(path, content.getBytes(), StandardOpenOption.CREATE,
        StandardOpenOption.TRUNCATE_EXISTING);

      System.out.println(content);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private Map<String, String> buildCustomParameters(Parameters parameters,
    Map<String, String> params) {

    Map<String, String> customParams = Maps.newHashMap(params);
    customParams.put("TABLE_NAME", parameters.getEntityNameLowerUnderscore());
    customParams.put("TABLE_FIELDS", buildFields(parameters));

    return customParams;
  }

  private String buildFields(Parameters parameters) {
    StringBuilder builder = new StringBuilder();

    for (EntityField field : parameters.getFields()) {
      builder.append("<column name=\"");
      builder.append(field.getTableNameLowerCase());
      builder.append("\" type=\"");
      builder.append(field.getJdbcType());
      builder.append("\">\n\t<constraints ");

      builder.append("nullable=\"");
      builder.append(field.isNullable());
      builder.append("\"");

      builder.append("/>\n</column>");
    }

    return builder.toString();
  }
}
