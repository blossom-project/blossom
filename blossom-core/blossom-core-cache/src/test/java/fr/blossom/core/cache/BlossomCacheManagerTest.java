package fr.blossom.core.cache;

import com.google.common.collect.Lists;
import fr.blossom.core.cache.CacheConfig.CacheConfigBuilder;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache;
import org.springframework.plugin.core.SimplePluginRegistry;

@RunWith(MockitoJUnitRunner.class)
public class BlossomCacheManagerTest {

  @Mock
  public SimplePluginRegistry<CacheConfig, String> simplePluginRegistry;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_not_accept_null_environment() {
    thrown.expect(NullPointerException.class);
    new BlossomCacheManager(null, CacheConfigBuilder.create("test").build());
  }

  @Test
  public void should_not_accept_null_default_cache_configuration() {
    thrown.expect(NullPointerException.class);
    new BlossomCacheManager(simplePluginRegistry, null);
  }

  @Test
  public void should_accept_init() {
    new BlossomCacheManager(simplePluginRegistry, CacheConfigBuilder.create("test").build());
  }

  @Test
  public void should_get_cache(){
    Mockito.when(simplePluginRegistry.getPluginFor(Mockito.anyString(), Mockito.any(CacheConfig.class))).thenAnswer(a -> a.getArgument(1));

    Cache cache = new BlossomCacheManager(simplePluginRegistry, CacheConfigBuilder.create("default").build()).getCache("test");
    Assert.assertNotNull("Cache should not be null", cache);
  }

  @Test
  public void should_load_all_caches() {
    List<String> cacheConfigs = Lists.newArrayList("cache", "cache2", "cache3");
    Mockito.when(simplePluginRegistry.getPlugins()).thenReturn(cacheConfigs.stream().map(name -> CacheConfigBuilder.create(name).build()).collect(Collectors.toList()));
    Mockito.when(simplePluginRegistry.getPluginFor(Mockito.anyString(), Mockito.any(CacheConfig.class))).then(args -> CacheConfigBuilder.create(args.getArgument(0)).build());

    BlossomCacheManager cacheManager = new BlossomCacheManager(simplePluginRegistry, CacheConfigBuilder.create("default").build());

    Collection<? extends Cache> caches = cacheManager.loadCaches();
    Assert.assertNotNull("Caches should not be null", caches);
    Assert.assertEquals("Number of caches should be equal to the number of plugins", caches.size(),
      cacheConfigs.size());
    Assert.assertTrue("Loaded caches should be named correctly",
      cacheConfigs.containsAll(caches.stream().map(Cache::getName).collect(Collectors.toList())));

  }


  @Test
  public void should_not_get_missing_cache_with_default_name() {
    CacheConfig defaultSpecification = CacheConfigBuilder.create("default").specification("expireAfterWrite=5m").build();
    BlossomCacheManager cacheManager = new BlossomCacheManager(simplePluginRegistry, defaultSpecification);
    Cache cache = cacheManager.getMissingCache(defaultSpecification.cacheName());
    Assert.assertNull("Cache should be null", cache);
  }

  @Test
  public void should_get_missing_cache_with_default_cache_configuration() {
    CacheConfig defaultSpecification = CacheConfigBuilder.create("default").specification("expireAfterWrite=5m").build();
    BlossomCacheManager cacheManager = new BlossomCacheManager(simplePluginRegistry, defaultSpecification);

    Mockito.when(simplePluginRegistry.getPluginFor(Mockito.anyString(), Mockito.any(CacheConfig.class))).thenReturn(defaultSpecification);

    Cache cache = cacheManager.getMissingCache("cache");
    Assert.assertNotNull("Cache should not be null", cache);
    Assert.assertTrue("Cache should not be a BlossomCache", cache instanceof BlossomCache);
    Assert.assertEquals("Cache should have the default configuration", ((BlossomCache) cache).getConfiguration().specification(), defaultSpecification.specification());
  }

  @Test
  public void should_get_missing_cache_with_specific_cache_configuration() {
    CacheConfig testSpecification = CacheConfigBuilder.create("test").specification("expireAfterWrite=5m").build();
    CacheConfig defaultSpecification = CacheConfigBuilder.create("default").specification("defaultSpecification").build();
    BlossomCacheManager cacheManager = new BlossomCacheManager(simplePluginRegistry, defaultSpecification);

    Mockito.when(simplePluginRegistry.getPluginFor(Mockito.anyString(), Mockito.any(CacheConfig.class))).thenReturn(testSpecification);

    Cache cache = cacheManager.getMissingCache("cache");
    Assert.assertNotNull("Cache should not be null", cache);
    Assert.assertTrue("Cache should not be a BlossomCache", cache instanceof BlossomCache);
    Assert.assertEquals("Cache should have the default configuration", ((BlossomCache) cache).getConfiguration().specification(), testSpecification.specification());

  }

}
