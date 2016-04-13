package fr.mgargadennec.blossom.core.common.support.message;

import java.io.IOException;
import java.util.Properties;

import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

/**
 * This class extends @{ReloadableResourceBundleMessageSource} to allow loading and merging properties from files
 * contained multiple times within the classpath, under the same subpath.
 *
 * To use, just configure it like @{ReloadableResourceBundleMessageSource} but with a Spring EL path (ex :
 * 'classpath*:path/to/my/file').
 *
 * @author Ma�l Gargadennnec
 */
public class BlossomReloadableResourceBundleMultipleMessageSource extends ReloadableResourceBundleMessageSource {

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
      Resource[] resources = resolver.getResources(filename + PROPERTIES_SUFFIX);
      for (Resource resource : resources) {
        String sourcePath = resource.getURI().toString().replace(PROPERTIES_SUFFIX, "");
        PropertiesHolder holder = super.refreshProperties(sourcePath, propHolder);
        properties.putAll(holder.getProperties());
        if (lastModified < resource.lastModified()) {
          lastModified = resource.lastModified();
        }
      }
    } catch (IOException ignored) {
      logger.warn("Can't fetch properties for basenames " + filename, ignored);
    }
    return new PropertiesHolder(properties, lastModified);
  }
}