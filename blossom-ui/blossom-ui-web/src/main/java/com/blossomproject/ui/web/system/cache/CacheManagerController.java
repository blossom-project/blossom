package com.blossomproject.ui.web.system.cache;

import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.common.collect.Maps;
import com.blossomproject.core.cache.BlossomCache;
import com.blossomproject.core.cache.BlossomCacheManager;
import com.blossomproject.ui.menu.OpenedMenu;
import com.blossomproject.ui.stereotype.BlossomController;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by maelg on 10/05/2017.
 */
@BlossomController
@RequestMapping("/system/caches")
@OpenedMenu("cacheManager")
@PreAuthorize("hasAuthority('system:caches:manager')")
public class CacheManagerController {
  private final static Logger LOGGER = LoggerFactory.getLogger(CacheManagerController.class);
  private final BlossomCacheManager cacheManager;

  public CacheManagerController(BlossomCacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @GetMapping
  public ModelAndView caches(
    @RequestParam(name = "q", defaultValue = "", required = false) String q, Model model) {
    Map<String, Map<String, Object>> caches = Maps.newHashMap();
    cacheManager.getCacheNames().stream()
      .map(n -> (BlossomCache) this.cacheManager.getCache(n))
      .filter(cache -> StringUtils.isEmpty(q) || cache.getName().contains(q))
      .forEach(cache -> {
          CacheStats stats = cache.getNativeCache().stats();
          Map<String, Object> data = Maps.newHashMap();
          data.put("cache", cache);
          data.put("size", cache.getNativeCache().estimatedSize());
          data.put("hits", stats.hitCount());
          data.put("misses", stats.missCount());
          data.put("evictions", stats.evictionCount());

          caches.put(cache.getName(), data);
        }
      );

    model.addAttribute("caches", caches);
    model.addAttribute("q", q);

    return new ModelAndView("blossom/system/caches/caches", model.asMap());
  }

  @PostMapping("/{name}/_empty")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void emptyCache(@PathVariable("name") String name) {
    org.springframework.cache.Cache cache = cacheManager.getCache(name);
    if (cache != null) {
      cacheManager.getCache(name).clear();
    }
  }

  @PostMapping("/{name}/_disable")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void disableCache(@PathVariable("name") String name) {
    BlossomCache cache = (BlossomCache) cacheManager.getCache(name);
    if (cache != null) {
      cache.disable();
    }
  }

  @PostMapping("/{name}/_enable")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void enableCache(@PathVariable("name") String name) {
    BlossomCache cache = (BlossomCache) cacheManager.getCache(name);
    if (cache != null) {
      cache.enable();
    }
  }
}
