package fr.mgargadennec.blossom.ui.web.system.cache;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by maelg on 10/05/2017.
 */
@BlossomController("/system/cache")
public class CacheManagerController {
    private final static Logger LOGGER = LoggerFactory.getLogger(CacheManagerController.class);
    private final CacheManager cacheManager;


    public CacheManagerController(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    @GetMapping
    public ModelAndView dashboard() {
        cacheManager.getCacheNames().stream().map(n-> this.cacheManager.getCache(n)).forEach(cache ->{
            if(cache.getNativeCache() instanceof com.github.benmanes.caffeine.cache.Cache){
                com.github.benmanes.caffeine.cache.Cache caffeineCache = (com.github.benmanes.caffeine.cache.Cache) cache.getNativeCache();
                LOGGER.info("Cache stats {}", caffeineCache.stats());
            }
        });
        return new ModelAndView("system/dashboard/dashboard");
    }

}
