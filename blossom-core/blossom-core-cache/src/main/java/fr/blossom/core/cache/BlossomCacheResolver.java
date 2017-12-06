package fr.blossom.core.cache;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import java.util.Collection;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.AbstractCacheResolver;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;

/**
 * Created by maelg on 12/05/2017.
 */
public class BlossomCacheResolver extends AbstractCacheResolver {

  public BlossomCacheResolver(CacheManager cacheManager) {
    super(cacheManager);
    Preconditions.checkNotNull(cacheManager);
  }

  @Override
  protected Collection<String> getCacheNames(CacheOperationInvocationContext<?> context) {
    if (context.getOperation().getCacheNames().isEmpty()) {
      return Lists.newArrayList(context.getTarget().getClass().getCanonicalName());
    }
    return context.getOperation().getCacheNames();
  }
}
