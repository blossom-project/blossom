package fr.mgargadennec.blossom.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.CaffeineSpec;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

/**
 * Created by maelg on 12/05/2017.
 */
public class BlossomCacheManager extends CaffeineCacheManager {

    private final Environment environment;

    public BlossomCacheManager(Environment environment) {
        this.environment = environment;
    }

    @Override
    protected Cache<Object, Object> createNativeCaffeineCache(String name) {
        String cacheSpec = environment.getProperty("blossom.cache."+name+".spec","");
        if (StringUtils.hasText(cacheSpec)) {
            Caffeine caffeine = Caffeine.from(CaffeineSpec.parse(cacheSpec));
            caffeine.recordStats();
            return caffeine.build();
        }else{
           return super.createNativeCaffeineCache(name);
        }
    }

}
