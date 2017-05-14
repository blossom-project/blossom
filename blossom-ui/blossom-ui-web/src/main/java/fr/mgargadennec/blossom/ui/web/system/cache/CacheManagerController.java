package fr.mgargadennec.blossom.ui.web.system.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.common.collect.Maps;
import fr.mgargadennec.blossom.core.cache.BlossomCacheManager;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by maelg on 10/05/2017.
 */
@BlossomController("/system/caches")
public class CacheManagerController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CacheManagerController.class);
    private final BlossomCacheManager cacheManager;


    public CacheManagerController(BlossomCacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping
    public ModelAndView dashboard() {
        Map<String, CacheStats> caches = Maps.newHashMap();
        cacheManager.getCacheNames().stream().map(n -> this.cacheManager.getCache(n)).forEach(cache -> {
            caches.put(cache.getName(), ((Cache) cache.getNativeCache()).stats());
        });
        return new ModelAndView("system/caches/caches", "caches", caches);
    }

}
