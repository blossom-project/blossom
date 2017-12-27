package fr.blossom.core.cache;

import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.interceptor.BasicOperation;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;

@RunWith(MockitoJUnitRunner.class)
public class BlossomCacheResolverTest {

  @Mock
  public BlossomCacheManager blossomCacheManager;

  @Mock
  public CacheOperationInvocationContext<?> context;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private BlossomCacheResolver blossomCacheResolver;

  @Test
  public void should_not_accept_null_cache_manager() {
    thrown.expect(NullPointerException.class);
    new BlossomCacheResolver(null);
  }

  @Test
  public void should_accept_cache_manager() {
    blossomCacheResolver = new BlossomCacheResolver(blossomCacheManager);
  }

  @Test
  public void should_resolve_caches_with_specified_names() {
    blossomCacheResolver = new BlossomCacheResolver(blossomCacheManager);
    Set<String> cacheNames = Sets.newHashSet("cache1", "cache2");

    BasicOperation operation = Mockito.mock(BasicOperation.class);
    Mockito.when(operation.getCacheNames()).thenReturn(cacheNames);
    Mockito.when(context.getOperation()).then(args -> operation);

    Collection<? extends String> caches = blossomCacheResolver.getCacheNames(context);

    Assert.assertTrue("Named cache should be found", caches.containsAll(cacheNames));

  }

  @Test
  public void should_resolve_caches_without_specified_names() {
    blossomCacheResolver = new BlossomCacheResolver(blossomCacheManager);

    BasicOperation operation = Mockito.mock(BasicOperation.class);
    Mockito.when(operation.getCacheNames()).thenReturn(Sets.newHashSet());
    Mockito.when(context.getOperation()).then(args -> operation);
    Mockito.when(context.getTarget()).thenReturn(this);

    Collection<? extends String> caches = blossomCacheResolver.getCacheNames(context);

    Assert.assertTrue("Non name cache should return target canonical name", caches.contains(this.getClass().getCanonicalName()));

  }

}
