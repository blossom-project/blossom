package fr.blossom.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import java.util.concurrent.Callable;
import org.springframework.cache.caffeine.CaffeineCache;

public class BlossomCache extends CaffeineCache {
  private final CacheConfig configuration;
  private boolean enabled;

  public BlossomCache(String name, CacheConfig configuration, Cache<Object, Object> cache) {
    super(name, cache);
    this.configuration = configuration;
    this.enabled = true;
  }

  public BlossomCache(String name, CacheConfig configuration, Cache<Object, Object> cache, boolean allowNullValues) {
    super(name, cache, allowNullValues);
    this.configuration = configuration;
    this.enabled = true;
  }

  @Override
  public ValueWrapper get(Object key) {
    if (!enabled) {
      return null;
    }
    return super.get(key);
  }

  @Override
  public <T> T get(Object key, Class<T> type) {
    if (!enabled) {
      return null;
    }
    return super.get(key, type);
  }

  @Override
  public <T> T get(Object key, Callable<T> valueLoader) {
    if (!enabled) {
      return null;
    }
    return super.get(key, valueLoader);
  }

  @Override
  public void put(Object key, Object value) {
    if (!enabled) {
      return;
    }
    super.put(key, value);
  }

  @Override
  public ValueWrapper putIfAbsent(Object key, Object value) {
    if (!enabled) {
      return null;
    }
    return super.putIfAbsent(key, value);
  }


  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
    updateCacheState();
  }

  public boolean isEnabled() {
    return enabled;
  }

  public void enable() {
    this.enabled = true;
    updateCacheState();
  }

  public void disable() {
    this.enabled = false;
    updateCacheState();
  }

  private void updateCacheState() {
    if (!enabled) {
      this.clear();
    }
  }

  public CacheConfig getConfiguration() {
    return configuration;
  }
}
