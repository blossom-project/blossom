package fr.mgargadennec.blossom.core.common.message_source;

import com.google.common.collect.Lists;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

public class MultiClasspathReloadableResourceBundleMessageSource extends ReloadableResourceBundleMessageSource {

  private static final String PROPERTIES_SUFFIX = ".properties";

  private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

  @Override
  protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
    if (filename.startsWith(PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
      return refreshClassPathProperties(filename, propHolder);
    } else {
      return super.refreshProperties(filename, propHolder);
    }
  }

  private PropertiesHolder refreshClassPathProperties(String filename, PropertiesHolder propHolder) {
    Properties properties = new Properties();
    long lastModified = -1;
    try {
      List<Resource> resources = Lists.newArrayList(resolver.getResources(filename + PROPERTIES_SUFFIX));
      if (getBasenameSet().contains(filename)) {
       resources = resources.stream().filter(resource -> !resource.getFilename().contains("_")).collect(Collectors.toList());
      }

      for (Resource resource : resources) {
        String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
        PropertiesHolder holder = super.refreshProperties(sourcePath, propHolder);
        properties.putAll(holder.getProperties());
        if (lastModified < resource.lastModified())
          lastModified = resource.lastModified();
      }
    } catch (IOException ignored) {
    }
    return new PropertiesHolder(properties, lastModified);
  }
}
