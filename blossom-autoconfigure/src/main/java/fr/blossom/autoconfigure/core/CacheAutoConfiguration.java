package fr.blossom.autoconfigure.core;

import fr.blossom.core.cache.BlossomCacheManager;
import fr.blossom.core.cache.BlossomCacheResolver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;

/**
 * Created by maelg on 10/05/2017.
 */
@Configuration
@EnableCaching
@PropertySource("classpath:/cache.properties")
public class CacheAutoConfiguration {
    private final static Logger LOGGER = LoggerFactory.getLogger(CacheAutoConfiguration.class);

    @Bean
    public CacheResolver blossomCacheResolver(final CacheManager cacheManager) {
        return new BlossomCacheResolver(cacheManager);
    }

    @Bean
    public CacheManager blossomCacheManager( Environment environment){
        return new BlossomCacheManager(environment);
    }
}
