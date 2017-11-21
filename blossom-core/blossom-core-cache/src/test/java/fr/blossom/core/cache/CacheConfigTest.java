package fr.blossom.core.cache;

import fr.blossom.core.cache.CacheConfig.CacheConfigBuilder;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

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
  public void should_supports_cache_name(){
    CacheConfig cacheConfig = CacheConfigBuilder.create("test").specification("expireAfterWrites=5m").build();
    Assert.assertTrue("Should support cache name", cacheConfig.supports("test"));

  }
}
