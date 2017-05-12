package fr.mgargadennec.blossom.autoconfigure.ui.web.system;

import fr.mgargadennec.blossom.ui.menu.MenuItem;
import fr.mgargadennec.blossom.ui.menu.MenuItemBuilder;
import fr.mgargadennec.blossom.ui.web.system.build.BuildInfoController;
import fr.mgargadennec.blossom.ui.web.system.cache.CacheManagerController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.info.BuildProperties;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(CacheManagerController.class)
public class WebSystemCacheAutoConfiguration {

  @Bean
  public MenuItem systemCacheMenuItem(MenuItemBuilder builder, @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
    return builder.key("cacheManager").label("menu.system.cache", true).link("/blossom/system/cache").order(3).icon("fa fa-cubes").parent(systemMenuItem).build();
  }


  @Bean
  public CacheManagerController cacheManagerController(CacheManager cacheManager) {
    return new CacheManagerController(cacheManager);
  }

}
