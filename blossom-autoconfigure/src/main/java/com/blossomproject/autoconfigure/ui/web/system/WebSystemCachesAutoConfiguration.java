package com.blossomproject.autoconfigure.ui.web.system;

import com.blossomproject.autoconfigure.core.CacheAutoConfiguration.BlossomCacheAutoConfiguration;
import com.blossomproject.core.cache.BlossomCacheManager;
import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.ui.menu.MenuItem;
import com.blossomproject.ui.menu.MenuItemBuilder;
import com.blossomproject.ui.web.system.cache.CacheManagerController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@AutoConfigureAfter(BlossomCacheAutoConfiguration.class)
@ConditionalOnClass(CacheManagerController.class)
@ConditionalOnBean(BlossomCacheManager.class)
public class WebSystemCachesAutoConfiguration {

  @Bean
  public MenuItem systemCacheMenuItem(MenuItemBuilder builder,
    @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder
      .key("cacheManager")
      .label("menu.system.caches")
      .link("/blossom/system/caches")
      .icon("fa fa-magnet")
      .order(2)
      .privilege(cacheManagerPrivilegePlugin())
      .parent(systemMenuItem)
      .build();
  }


  @Bean
  public CacheManagerController cacheManagerController(BlossomCacheManager cacheManager) {
    return new CacheManagerController(cacheManager);
  }


  @Bean
  public Privilege cacheManagerPrivilegePlugin() {
    return new SimplePrivilege("system", "caches", "manager");
  }

}
