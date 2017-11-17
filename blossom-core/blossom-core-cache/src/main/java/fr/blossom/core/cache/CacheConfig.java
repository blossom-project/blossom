package fr.blossom.core.cache;

import com.google.common.collect.Sets;
import fr.blossom.core.common.PluginConstants;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;


@Qualifier(PluginConstants.PLUGIN_CACHE_CONFIGURATION)
public interface CacheConfig extends Plugin<String> {

  String specification();

  String[] linkedCaches();

  default Boolean enabled() {
    return true;
  }

  class CacheConfigImpl implements CacheConfig {
    private final String cacheName;
    private final String specification;
    private final String[] linkedCaches;

    private CacheConfigImpl(String cacheName, String specification, String... linkedCaches) {
      this.cacheName = cacheName;
      this.specification = specification;
      this.linkedCaches = linkedCaches;
    }

    @Override
    public boolean supports(String delimiter) {
      return delimiter.equals(cacheName);
    }

    @Override
    public String specification() {
      return specification;
    }

    @Override
    public String[] linkedCaches() {
      return linkedCaches;
    }
  }

  class CacheConfigBuilder {

    private final String cacheName;
    private String specification = "";
    private Set<String> linkedCaches = Sets.newHashSet();

    private CacheConfigBuilder(String cacheName) {
      this.cacheName = cacheName;
    }

    public static CacheConfigBuilder create(String cacheName) {
      return new CacheConfigBuilder(cacheName);
    }

    public CacheConfigBuilder specification(String specification) {
      this.specification = specification;
      return this;
    }

    public CacheConfigBuilder addLinkedCache(String linkedCache) {
      this.linkedCaches.add(linkedCache);
      return this;
    }

    public CacheConfig build() {
      return new CacheConfigImpl(cacheName, specification, linkedCaches.toArray(new String[linkedCaches.size()]));
    }
  }
}
