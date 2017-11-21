package fr.blossom.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.transaction.AbstractTransactionSupportingCacheManager;
import org.springframework.plugin.core.PluginRegistry;

/**
 * Created by maelg on 12/05/2017.
 */
public class BlossomCacheManager extends AbstractTransactionSupportingCacheManager {
  private final static Logger logger = LoggerFactory.getLogger(BlossomCacheManager.class);
  private final PluginRegistry<CacheConfig, String> registry;
  private final CacheConfig defaultCacheConfiguration;

  public BlossomCacheManager(PluginRegistry<CacheConfig, String> registry, CacheConfig defaultCacheConfiguration) {
    Preconditions.checkNotNull(registry);
    Preconditions.checkNotNull(defaultCacheConfiguration);
    this.registry = registry;
    this.defaultCacheConfiguration= defaultCacheConfiguration;
  }

  @Override
  protected Collection<? extends org.springframework.cache.Cache> loadCaches() {
    return this.registry.getPlugins().stream().map(cacheConfig -> getMissingCache(cacheConfig.cacheName())).collect(
      Collectors.toList());
  }

  @Override
  public org.springframework.cache.Cache getCache(String name) {
    return super.getCache(name);
  }

  @Override
  protected org.springframework.cache.Cache getMissingCache(String name) {
    return createBlossomCache(name);
  }

  protected org.springframework.cache.Cache createBlossomCache(String name) {
    CacheConfig config = registry.getPluginFor(name, defaultCacheConfiguration);

    BlossomCache cache = new BlossomCache(name, config, createNativeBlossomCache(name, config.specification(), config.linkedCaches()));
    cache.setEnabled(config.enabled());
    return cache;
  }

  protected Cache<Object, Object> createNativeBlossomCache(String name, String cacheSpecification, String[] linkedCaches) {
    Caffeine caffeine = Caffeine.from(CaffeineSpec.parse(cacheSpecification));
    caffeine.removalListener(new BlossomCacheRemovalListener(this, name, linkedCaches));
    return caffeine.build();
  }
}
