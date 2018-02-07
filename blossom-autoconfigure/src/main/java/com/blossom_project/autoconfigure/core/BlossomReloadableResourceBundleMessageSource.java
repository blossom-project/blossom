package com.blossom_project.autoconfigure.core;

import java.io.IOException;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class BlossomReloadableResourceBundleMessageSource extends
  ReloadableResourceBundleMessageSource {

  private static final String PROPERTIES_SUFFIX = ".properties";

  private final ConcurrentMap<String, PropertiesHolder> cachedClasspathProperties =
    new ConcurrentHashMap<>();

  private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

  @Override
  protected PropertiesHolder refreshProperties(String filename, PropertiesHolder propHolder) {
    if (filename.startsWith(PathMatchingResourcePatternResolver.CLASSPATH_ALL_URL_PREFIX)) {
      PropertiesHolder existingHolder = this.cachedClasspathProperties.get(filename);
      if (existingHolder != null && existingHolder.getRefreshTimestamp() > (
        System.currentTimeMillis() - getCacheMillis())) {
        return existingHolder;
      }
      return refreshClassPathProperties(filename, propHolder);
    } else {
      return super.refreshProperties(filename, propHolder);
    }
  }

  private PropertiesHolder refreshClassPathProperties(String filename,
    PropertiesHolder propHolder) {
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
    }
    PropertiesHolder holder = new PropertiesHolder(properties, lastModified);
    holder.setRefreshTimestamp(getCacheMillis() < 0 ? -1 : System.currentTimeMillis());
    cachedClasspathProperties.put(filename, holder);
    return holder;
  }
}
