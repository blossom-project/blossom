package fr.mgargadennec.blossom.ui.web.system.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.stats.CacheStats;
import com.google.common.collect.Maps;
import fr.mgargadennec.blossom.core.cache.BlossomCacheManager;
import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by maelg on 10/05/2017.
 */
@BlossomController("/system/caches")
@OpenedMenu("cacheManager")
@PreAuthorize("hasAuthority('system:caches:manager')")
public class CacheManagerController {
  private final static Logger LOGGER = LoggerFactory.getLogger(CacheManagerController.class);
  private final BlossomCacheManager cacheManager;


  public CacheManagerController(BlossomCacheManager cacheManager) {
    this.cacheManager = cacheManager;
  }

  @GetMapping
  public ModelAndView caches(@RequestParam(name = "q", defaultValue = "", required = false) String q, Model model) {
    Map<String, Map<String, Object>> caches = Maps.newHashMap();
    cacheManager.getCacheNames().stream().map(n -> this.cacheManager.getCache(n)).forEach(cache -> {
      if (StringUtils.isEmpty(q) || cache.getName().contains(q)) {
        CacheStats stats = ((Cache) cache.getNativeCache()).stats();
        Map<String, Object> data = Maps.newHashMap();
        data.put("stats", stats);
        data.put("size", ((Cache) cache.getNativeCache()).estimatedSize());

        caches.put(cache.getName(), data);
      }
    });

    model.addAttribute("caches", caches);
    model.addAttribute("q", q);

    return new ModelAndView("system/caches/caches", model.asMap());
  }

  @PostMapping("/{name}/_empty")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void emptyCache(@PathVariable("name") String name) {
    org.springframework.cache.Cache cache =cacheManager.getCache(name);
    if (cache != null) {
      cacheManager.getCache(name).clear();
    }
  }
}
