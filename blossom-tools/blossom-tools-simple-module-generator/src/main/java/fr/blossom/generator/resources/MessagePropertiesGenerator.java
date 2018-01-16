package fr.blossom.generator.resources;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import fr.blossom.generator.configuration.model.Settings;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;

public class MessagePropertiesGenerator implements ResourceGenerator {

  @Override
  public void prepare(Settings settings) {

  }

  @Override
  public void generate(Settings settings, Map<String, String> params) {
    try {
      URL url = Resources.getResource("messages.properties");
      String content = Resources.toString(url, Charsets.UTF_8);

      for(Entry<String,String> entry : params.entrySet()){
        content = content.replaceAll("%%"+entry.getKey()+"%%", entry.getValue());
      }

      Path messageRoot = settings.getResourcePath().resolve("messages");
      Files.createDirectories(messageRoot);

      Files.write(messageRoot.resolve(settings.getEntityNameLowerHyphen()+".properties"), content.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);

    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
