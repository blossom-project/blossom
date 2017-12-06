package fr.blossom.core.cache;

import com.google.common.base.Preconditions;
import com.google.common.base.Strings;
import com.google.common.collect.Sets;
import fr.blossom.core.common.PluginConstants;
import java.util.Set;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.plugin.core.Plugin;


@Qualifier(PluginConstants.PLUGIN_CACHE_CONFIGURATION)
public interface CacheConfig extends Plugin<String> {

  String cacheName();

  String specification();

  String[] linkedCaches();

  default Boolean enabled() {
    return true;
  }

  class CacheConfigImpl implements CacheConfig {

    private final String cacheName;
    private final String specification;
    private final Boolean enabled;
    private final String[] linkedCaches;

    private CacheConfigImpl(String cacheName, String specification, boolean enabled,
      String... linkedCaches
    ) {
      this.cacheName = cacheName;
      this.specification = specification;
      this.linkedCaches = linkedCaches;
      this.enabled = enabled;
    }

    @Override
    public boolean supports(String delimiter) {
      return delimiter.equals(cacheName);
    }

    @Override
    public String cacheName() {
      return cacheName;
    }

    @Override
    public String specification() {
      return specification;
    }

    @Override
    public String[] linkedCaches() {
      return linkedCaches;
    }

    @Override
    public Boolean enabled() {
      return enabled;
    }
  }

  class CacheConfigBuilder {

    private final String cacheName;
    private String specification = "";
    private boolean enabled = true;
    private Set<String> linkedCaches = Sets.newHashSet();

    private CacheConfigBuilder(String cacheName) {
      this.cacheName = cacheName;
    }

    public static CacheConfigBuilder create(String cacheName) {
      Preconditions
        .checkArgument(!Strings.isNullOrEmpty(cacheName), "Cache name should not be null !");
      return new CacheConfigBuilder(cacheName);
    }

    public CacheConfigBuilder specification(String specification) {
      this.specification = specification;
      return this;
    }

    public CacheConfigBuilder enabled(boolean enabled) {
      this.enabled = enabled;
      return this;
    }

    public CacheConfigBuilder addLinkedCache(String linkedCache) {
      this.linkedCaches.add(linkedCache);
      return this;
    }

    public CacheConfig build() {
      return new CacheConfigImpl(cacheName, specification, enabled,
        linkedCaches.toArray(new String[linkedCaches.size()]));
    }
  }
}
