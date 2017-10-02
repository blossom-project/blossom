package fr.mgargadennec.blossom.simple_module_generator.views;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import fr.mgargadennec.blossom.simple_module_generator.Parameters;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Map.Entry;

public class MessagePropertiesGenerator implements ResourceGenerator{

  @Override
  public void generate(Path path, Parameters parameters, Map<String, String> params) {
    try {
      URL url = Resources.getResource("messages.properties");
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
