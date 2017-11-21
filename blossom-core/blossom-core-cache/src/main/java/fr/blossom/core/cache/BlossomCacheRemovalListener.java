package fr.blossom.core.cache;

import com.github.benmanes.caffeine.cache.RemovalCause;
import com.github.benmanes.caffeine.cache.RemovalListener;
import com.google.common.base.Preconditions;
import fr.blossom.core.common.entity.AbstractEntity;
import java.util.concurrent.atomic.AtomicInteger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BlossomCacheRemovalListener implements RemovalListener<Object, Object> {
  private final Logger logger = LoggerFactory.getLogger(BlossomCacheRemovalListener.class);

  private final BlossomCacheManager blossomCacheManager;
  private final String cacheName;
  private final String[] linkedCaches;

  BlossomCacheRemovalListener(BlossomCacheManager blossomCacheManager, String cacheName, String... linkedCaches) {
    Preconditions.checkNotNull(blossomCacheManager);
    Preconditions.checkNotNull(cacheName);

    this.blossomCacheManager = blossomCacheManager;
    this.cacheName = cacheName;
    this.linkedCaches = linkedCaches;
  }

  @Override
  public void onRemoval(Object key, Object value, RemovalCause cause) {
    if(logger.isDebugEnabled()){
      logger.debug("Cache {} removed key {} with cause {} (object was of type {})", this.cacheName, key , cause, value.getClass());
    }

    if(!cause.wasEvicted()) {
      if (value instanceof AbstractEntity) {
        BlossomCache cache = (BlossomCache) this.blossomCacheManager.getCache(this.cacheName);
        AtomicInteger i = new AtomicInteger(0);

        cache.getNativeCache().asMap().forEach((k, v) -> {
          if (!(v instanceof AbstractEntity)) {
            cache.evict(k);
            i.incrementAndGet();
          }
        });

        if(logger.isDebugEnabled()){
          logger.debug("Cache {} removed {} objects from cache which where not AbstractEntities", this.cacheName, i.get());
        }

        for (String linkedCache : linkedCaches) {
          this.blossomCacheManager.getCache(linkedCache).clear();
          if (logger.isDebugEnabled()) {
            logger.debug("Cache {} clearing linked cache {}", linkedCache);
          }
        }
      }
    }
  }
}
