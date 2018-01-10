package fr.blossom.autoconfigure.ui.web.system;

import fr.blossom.core.cache.BlossomCacheManager;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.ui.menu.MenuItem;
import fr.blossom.ui.menu.MenuItemBuilder;
import fr.blossom.ui.web.system.cache.CacheManagerController;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(CacheManagerController.class)
public class WebSystemCachesAutoConfiguration {

    @Bean
    public MenuItem systemCacheMenuItem(MenuItemBuilder builder, @Qualifier("systemMenuItem") MenuItem systemMenuItem) {
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
