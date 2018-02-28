package com.blossomproject.generator.resources;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import com.google.common.io.Resources;
import com.blossomproject.generator.configuration.model.Field;
import com.blossomproject.generator.configuration.model.Settings;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.Map.Entry;

public class ChangelogGenerator implements ResourceGenerator {

  private Path changelogDir;
  private Path fullChangelogPath;

  private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HHmmss");

  @Override
  public void prepare(Settings settings) {

    try {
      changelogDir = settings.getResourcePath().resolve("db").resolve("changelog").resolve("generated");
      Files.createDirectories(changelogDir);
      fullChangelogPath = changelogDir.resolve( "4_db.changelog_blossom_generated_" + settings.getEntityNameLowerUnderscore() + ".xml");
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  @Override
  public void generate(Settings settings, Map<String, String> params) {
    try {
      URL url = Resources.getResource("changelog.xml");
      String content = Resources.toString(url, Charsets.UTF_8);

      Map<String, String> customParams = buildCustomParameters(settings, params);

      for (Entry<String, String> entry : customParams.entrySet()) {
        content = content.replaceAll("%%" + entry.getKey() + "%%", entry.getValue());
      }
      Files.write(fullChangelogPath, content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private Map<String, String> buildCustomParameters(Settings settings,
    Map<String, String> params) {

    Map<String, String> customParams = Maps.newHashMap(params);
    customParams.put("TABLE_NAME", settings.getEntityNameLowerUnderscore());
    customParams.put("TABLE_FIELDS", buildFields(settings));

    return customParams;
  }

  private String buildFields(Settings settings) {
    StringBuilder builder = new StringBuilder();

    for (Field field : settings.getFields()) {
      builder.append("<column name=\"");
      builder.append(field.getColumnName().toLowerCase());
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
