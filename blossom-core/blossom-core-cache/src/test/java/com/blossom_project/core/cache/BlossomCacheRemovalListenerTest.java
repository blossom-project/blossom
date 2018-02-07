package com.blossom_project.core.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.RemovalCause;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.blossom_project.core.cache.CacheConfig.CacheConfigBuilder;
import com.blossom_project.core.common.entity.AbstractEntity;
import java.util.concurrent.ConcurrentMap;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class BlossomCacheRemovalListenerTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Mock
  public BlossomCacheManager blossomCacheManager;

  @Mock
  Cache<Object, Object> nativeCache;

  private BlossomCacheRemovalListener removalListener;

  @Test
  public void should_not_accept_null_environment() {
    thrown.expect(NullPointerException.class);
    removalListener = new BlossomCacheRemovalListener(null, "test", "test2", "test3");
  }

  @Test
  public void should_not_accept_null_cache_name() {
    thrown.expect(NullPointerException.class);
    removalListener = new BlossomCacheRemovalListener(blossomCacheManager, null, "test2", "test3");
  }

  @Test
  public void should_accept_null_linked_caches() {
    removalListener = new BlossomCacheRemovalListener(blossomCacheManager, "test");
  }

  @Test
  public void should_do_nothing_if_expired() {
    removalListener = new BlossomCacheRemovalListener(blossomCacheManager, "test");

    removalListener.onRemoval("test", "value", RemovalCause.EXPIRED);

    Mockito.verify(blossomCacheManager, Mockito.times(0)).getCache(Mockito.anyString());
  }

  @Test
  public void should_do_nothing_if_collected() {
    removalListener = new BlossomCacheRemovalListener(blossomCacheManager, "test");

    removalListener.onRemoval("test", "value", RemovalCause.COLLECTED);

    Mockito.verify(blossomCacheManager, Mockito.times(0)).getCache(Mockito.anyString());
  }

  @Test
  public void should_do_nothing_if_size() {
    removalListener = new BlossomCacheRemovalListener(blossomCacheManager, "test");

    removalListener.onRemoval("test", "value", RemovalCause.SIZE);

    Mockito.verify(blossomCacheManager, Mockito.times(0)).getCache(Mockito.anyString());
  }


  @Test
  public void should_do_nothing_if_evictedè_but_not_abstract_entity() {
    removalListener = new BlossomCacheRemovalListener(blossomCacheManager, "test");

    removalListener.onRemoval("test", "value", RemovalCause.REPLACED);

    Mockito.verify(blossomCacheManager, Mockito.times(0)).getCache(Mockito.anyString());
  }


  @Test
  public void should_clean_cache_if_evicted_and_abstract_entity() {
    String cacheName = "test";
    removalListener = new BlossomCacheRemovalListener(blossomCacheManager, cacheName);
    BlossomCache blossomCache = new BlossomCache("test", CacheConfigBuilder.create("test").build(),
      nativeCache, true);
    blossomCache = Mockito.spy(blossomCache);

    PageRequest toEvict = PageRequest.of(0, 10);

    ConcurrentMap<Object, Object> map = Maps.newConcurrentMap();
    map.put(toEvict, new PageImpl(Lists.newArrayList()));
    map.put("1", new TestAbstractEntity(1L));
    map.put("2", new TestAbstractEntity(2L));
    map.put("3", new TestAbstractEntity(3L));
    map.put("4", new TestAbstractEntity(4L));

    Mockito.when(blossomCacheManager.getCache(Mockito.anyString())).thenReturn(blossomCache);
    Mockito.when(nativeCache.asMap()).thenReturn(map);

    removalListener.onRemoval("1", new TestAbstractEntity(1L), RemovalCause.REPLACED);

    Mockito.verify(blossomCacheManager, Mockito.times(1)).getCache(Mockito.eq(cacheName));
    Mockito.verify(nativeCache, Mockito.times(1)).asMap();
    Mockito.verify(blossomCache, Mockito.times(1)).evict(Mockito.eq(toEvict));
  }


  @Test
  public void should_clean_linked_caches_if_evicted_and_abstract_entity() {
    String cacheName = "test";
    removalListener = new BlossomCacheRemovalListener(blossomCacheManager, cacheName, "linkedCache", "linkedCache2");
    BlossomCache blossomCache = new BlossomCache("test", CacheConfigBuilder.create("test").addLinkedCache("linkedCache").addLinkedCache("linkedCache2").build(), nativeCache, true);
    BlossomCache linkedCache = Mockito.mock(BlossomCache.class);
    BlossomCache linkedCache2 = Mockito.mock(BlossomCache.class);

    Mockito.when(blossomCacheManager.getCache(Mockito.eq("test"))).thenReturn(blossomCache);
    Mockito.when(blossomCacheManager.getCache(Mockito.eq("linkedCache"))).thenReturn(linkedCache);
    Mockito.when(blossomCacheManager.getCache(Mockito.eq("linkedCache2"))).thenReturn(linkedCache2);
    Mockito.when(nativeCache.asMap()).thenReturn(Maps.newConcurrentMap());

    removalListener.onRemoval("1", new TestAbstractEntity(1L), RemovalCause.REPLACED);

    Mockito.verify(blossomCacheManager, Mockito.times(1)).getCache(Mockito.eq(cacheName));
    Mockito.verify(nativeCache, Mockito.times(1)).asMap();
    Mockito.verify(linkedCache, Mockito.times(1)).clear();
    Mockito.verify(linkedCache2, Mockito.times(1)).clear();
  }


  private class TestAbstractEntity extends AbstractEntity {

    public TestAbstractEntity(Long id) {
      this.setId(id);
    }

  }
}
