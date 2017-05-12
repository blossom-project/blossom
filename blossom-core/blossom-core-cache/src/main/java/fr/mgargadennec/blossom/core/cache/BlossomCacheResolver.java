package fr.mgargadennec.blossom.core.cache;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.CacheResolver;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Created by maelg on 12/05/2017.
 */
public class BlossomCacheResolver implements CacheResolver {
    private final CacheManager cacheManager;

    public BlossomCacheResolver(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @Override
    public Collection<? extends org.springframework.cache.Cache> resolveCaches(CacheOperationInvocationContext<?> context) {
        List<Cache> caches = new ArrayList<>();
        if(context.getOperation().getCacheNames().isEmpty()){
            caches.add(cacheManager.getCache(context.getTarget().getClass().getCanonicalName()));
            return caches;
        }

        caches.add(cacheManager.getCache("nocache"));
        return caches;
    }
}
