package fr.blossom.core.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import fr.blossom.core.cache.CacheConfig.CacheConfigBuilder;
import fr.blossom.core.common.entity.AbstractEntity;
import java.util.UUID;
import java.util.concurrent.Callable;
import java.util.function.Function;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class BlossomCacheTest {

  private Cache cache;

  private BlossomCache blossomCache;

  @Before
  public void setUp() {
    cache = Mockito.spy(
      Caffeine.newBuilder()
        .removalListener((key, value, cause) -> System.out
          .format("removal listerner called with key [%s], cause [%s], evicted [%S]\n", key,
            cause.toString(), cause.wasEvicted()))
        .build());

    blossomCache = new BlossomCache("test", CacheConfigBuilder.create("test").build(), cache);
  }

  @After
  public void destroy() {
    blossomCache.clear();
  }


  @Test
  public void should_put_object_by_key_string() {
    String key = "test";
    blossomCache.put(key, key);

    Mockito.verify(cache, Mockito.times(1)).put(Mockito.eq(key), Mockito.eq(key));
  }

  @Test
  public void should_put_twice_by_key_string() {
    String key = "test";
    blossomCache.put(key, key);
    blossomCache.put(key, key);

    Mockito.verify(cache, Mockito.times(2)).put(Mockito.eq(key), Mockito.eq(key));
  }

  @Test
  public void should_put_object_if_absent_by_key_string() {
    String key = "test";
    blossomCache.put(key, key);
    blossomCache.putIfAbsent(key, key);

    Mockito.verify(cache, Mockito.times(1)).put(Mockito.eq(key), Mockito.eq(key));
    Mockito.verify(cache, Mockito.times(1)).get(Mockito.eq(key), Mockito.any(Function.class));
  }

  @Test
  public void should_get_object_by_key_string() {
    String key = "test";
    String value = "value";
    blossomCache.put(key, value);

    ValueWrapper valueFromCache = blossomCache.get(key);
    assertNotNull("'Key' should return a value", valueFromCache);
    assertNotNull("'Key' should return a non-null value", valueFromCache.get());
    assertEquals("'Key' should return the right value", value, valueFromCache.get());
  }

  @Test
  public void should_get_object_by_key_entity() {
    String key = UUID.randomUUID().toString();
    TestAbstractEntity value = new TestAbstractEntity(1L);
    blossomCache.put(key, value);

    ValueWrapper valueFromCache = blossomCache.get(key);
    assertNotNull("'Key' should return a value", valueFromCache);
    assertNotNull("'Key' should return a non-null value", valueFromCache.get());
    assertEquals("'Key' should return the right value", value, valueFromCache.get());
    assertEquals("'Key' should return the right class", TestAbstractEntity.class,
      valueFromCache.get().getClass());
    assertEquals("'Key' should return the right value", value.getId(),
      ((TestAbstractEntity) valueFromCache.get()).getId());
  }

  @Test
  public void should_get_object_by_key_page_request() {
    PageRequest key = new PageRequest(0, 5);
    Page<?> value = new PageImpl<>(Lists.newArrayList());
    blossomCache.put(key, value);

    ValueWrapper valueFromCache = blossomCache.get(key);
    assertNotNull("Key should return a value", valueFromCache);
    assertNotNull("Key should return a non-null value", valueFromCache.get());
    assertEquals("Key should return the right value", value, valueFromCache.get());
    assertTrue("Value should be an empty page",
      ((Page) valueFromCache.get()).getContent().isEmpty());
  }


  @Test
  public void should_get_object_with_class() {
    String key = "test";
    blossomCache.put(key, key);

    String valueFromCache = blossomCache.get(key, String.class);
    assertNotNull("Should not be null", valueFromCache);
    assertEquals("Should be equals to the inserted value", valueFromCache, key);
  }

  @Test
  public void should_get_object_with_value_loader_if_not_present() throws Exception {
    String key = "test";

    Callable<String> valueLoader = () -> "valueLoader";
    String valueFromCache = blossomCache.get(key, valueLoader);
    assertNotNull("Should not be null", valueFromCache);
    assertEquals("Should be equals to the value loader value", valueFromCache, valueLoader.call());
  }
  @Test
  public void should_get_object_with_value_loader_if_present() throws Exception {
    String key = "test";
    blossomCache.put(key,key);

    Callable<String> valueLoader = () -> "valueLoader";
    String valueFromCache = blossomCache.get(key, valueLoader);
    assertNotNull("Should not be null", valueFromCache);
    assertEquals("Should be equals to the already present value ", valueFromCache, key);
  }

  @Test
  public void should_not_get_object_when_disabled() {
    String key = "test";
    blossomCache.put(key, key);

    blossomCache.disable();

    ValueWrapper valueFromCache = blossomCache.get(key);
    assertNull("'Key' should return a null value", valueFromCache);
  }

  @Test
  public void should_not_get_object_with_class_when_disabled() {
    String key = "test";
    blossomCache.put(key, key);

    blossomCache.disable();

    String valueFromCache = blossomCache.get(key, String.class);
    assertNull("'Key' should return a null value", valueFromCache);
  }

  @Test
  public void should_not_get_object_with_value_loader_when_disabled() {
    String key = "test";
    blossomCache.put(key, key);
    blossomCache.disable();

    String valueFromCache = blossomCache.get(key, () -> "");
    assertNull("'Key' should return a null value", valueFromCache);
  }

  @Test
  public void should_not_put_object_when_disabled() {
    String key = "test";
    blossomCache.disable();
    blossomCache.put(key, key);
    Mockito.verify(cache, Mockito.times(0)).put(key, key);
  }

  @Test
  public void should_not_put_if_absent_object_when_disabled() {
    String key = "test";
    blossomCache.disable();
    blossomCache.putIfAbsent(key, key);
    Mockito.verify(cache, Mockito.times(0)).put(key, key);
  }

  @Test
  public void should_be_enabled(){
    blossomCache.enable();
    Assert.assertTrue(blossomCache.isEnabled());
  }

  @Test
  public void should_be_enabled_by_value(){
    blossomCache.setEnabled(true);
    Assert.assertTrue(blossomCache.isEnabled());
  }

  @Test
  public void should_be_disabled(){
    blossomCache.disable();
    Assert.assertTrue(!blossomCache.isEnabled());
    Mockito.verify(cache, Mockito.times(1)).invalidateAll();
  }
  @Test
  public void should_be_disabled_by_value(){
    blossomCache.setEnabled(false);
    Assert.assertTrue(!blossomCache.isEnabled());
    Mockito.verify(cache, Mockito.times(1)).invalidateAll();
  }



  private class TestAbstractEntity extends AbstractEntity {

    public TestAbstractEntity(Long id) {
      this.setId(id);
    }

  }
}
