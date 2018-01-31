package fr.blossom.core.cache;

import fr.blossom.core.cache.CacheConfig.CacheConfigBuilder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class CacheConfigTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_not_accept_null_name(){
    thrown.expect(IllegalArgumentException.class);
    CacheConfigBuilder.create(null).build();
  }

  @Test
  public void should_not_accept_empty_name(){
    thrown.expect(IllegalArgumentException.class);
    CacheConfigBuilder.create("").build();
  }

  @Test
  public void should_accept_empty_specification(){
    CacheConfig cacheConfig = CacheConfigBuilder.create("test").build();
    Assert.assertNotNull("Should not be null", cacheConfig);
  }

  @Test
  public void should_accept_empty_linked_cache(){
    CacheConfig cacheConfig = CacheConfigBuilder.create("test").specification("expireAfterWrites=5m").build();
    Assert.assertNotNull("Should not be null", cacheConfig);
  }


  @Test
  public void should_be_enabled_by_default(){
    Assert.assertTrue("Should be enabled", new CacheConfig() {
      @Override
      public String cacheName() {
        return "test";
      }

      @Override
      public String specification() {
        return "";
      }

      @Override
      public String[] linkedCaches() {
        return new String[0];
      }

      @Override
      public boolean supports(String delimiter) {
        return false;
      }
    }.enabled());
  }

  @Test
  public void should_be_enabled_by_default_builder(){
    CacheConfig cacheConfig = CacheConfigBuilder.create("test").specification("expireAfterWrites=5m").build();
    Assert.assertTrue("Should be enabled", cacheConfig.enabled());
  }

  @Test
  public void should_be_enabled(){
    CacheConfig cacheConfig = CacheConfigBuilder.create("test").specification("expireAfterWrites=5m").enabled(true).build();
    Assert.assertTrue("Should be enabled", cacheConfig.enabled());
  }

  @Test
  public void should_be_disabled(){
    CacheConfig cacheConfig = CacheConfigBuilder.create("test").specification("expireAfterWrites=5m").enabled(false).build();
    Assert.assertTrue("Should be enabled", !cacheConfig.enabled());
  }

  @Test
  public void should_supports_cache_name(){
    CacheConfig cacheConfig = CacheConfigBuilder.create("test").specification("expireAfterWrites=5m").build();
    Assert.assertTrue("Should support cache name", cacheConfig.supports("test"));
  }
}
