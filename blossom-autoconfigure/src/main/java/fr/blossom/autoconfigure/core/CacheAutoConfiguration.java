package fr.blossom.autoconfigure.core;

import fr.blossom.core.cache.BlossomCacheManager;
import fr.blossom.core.cache.BlossomCacheResolver;
import fr.blossom.core.cache.CacheConfig;
import fr.blossom.core.cache.CacheConfig.CacheConfigBuilder;
import fr.blossom.core.common.PluginConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;

/**
 * Created by maelg on 10/05/2017.
 */
@Configuration
@EnableCaching
@EnablePluginRegistries(CacheConfig.class)
public class CacheAutoConfiguration {

    private final static Logger LOGGER = LoggerFactory.getLogger(CacheAutoConfiguration.class);
    private final static String DEFAULT_CACHE_CONFIGURATION = "default";

    @Bean
    @ConditionalOnMissingBean(name = "defaultCacheConfig")
    public CacheConfig defaultCacheConfig() {
        return CacheConfigBuilder.create(DEFAULT_CACHE_CONFIGURATION).specification("recordStats").build();
    }

    @Configuration
    public static class BlossomCacheAutoConfiguration {

        @Autowired
        @Qualifier(PluginConstants.PLUGIN_CACHE_CONFIGURATION)
        private PluginRegistry<CacheConfig, String> registry;

        @Bean
        public CacheResolver blossomCacheResolver(final CacheManager cacheManager) {
            return new BlossomCacheResolver(cacheManager);
        }

        @Bean
        public CacheManager blossomCacheManager() {
            return new BlossomCacheManager(registry, registry.getPluginFor(DEFAULT_CACHE_CONFIGURATION));
        }

        @Configuration
        public static class BlossomCachingConfigurerSupport extends CachingConfigurerSupport {

            @Autowired
            private CacheManager cacheManager;
            @Autowired
            private CacheResolver cacheResolver;


            @Override
            public CacheResolver cacheResolver() {
                return cacheResolver;
            }

            @Override
            public CacheManager cacheManager() {
                return cacheManager;
            }

        }
    }


}
