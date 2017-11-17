package fr.blossom.core.cache;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.google.common.collect.Lists;
import fr.blossom.core.common.entity.AbstractEntity;
import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

@RunWith(MockitoJUnitRunner.class)
public class BlossomCacheTest {

  private static BlossomCache blossomCache;

  @BeforeClass
  public static void setUp() {
    blossomCache = new BlossomCache("test", Caffeine.newBuilder()
      .removalListener((key, value, cause) -> System.out.format("removal listerner called with key [%s], cause [%s], evicted [%S]\n", key,cause.toString(), cause.wasEvicted()))
      .build());
  }

  @After
  public void destroy() {
    blossomCache.clear();
  }

  @Test
  public void test_get_object_by_key_string() {
    String key = "test";
    String value = "value";
    blossomCache.put(key, value);

    ValueWrapper valueFromCache = blossomCache.get(key);
    assertNotNull("'Key' should return a value", valueFromCache);
    assertNotNull("'Key' should return a non-null value", valueFromCache.get());
    assertEquals("'Key' should return the right value", value, valueFromCache.get());
  }

  @Test
  public void test_get_object_by_key_entity() {
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
  public void test_get_object_by_key_page_request_and_empty_page() {
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
  public void test_get_object_by_key_page_request_and_page_of_long() {
    PageRequest key = new PageRequest(0, 5);
    Page<Long> value = new PageImpl<Long>(Lists.newArrayList(1L, 2L, 3L));
    blossomCache.put(key, value);

    ValueWrapper valueFromCache = blossomCache.get(key);
    assertNotNull("Key should return a value", valueFromCache);
    assertNotNull("Key should return a non-null value", valueFromCache.get());
    assertEquals("Key should return the right value", value, valueFromCache.get());
    assertTrue("Value should be a page of 3 strings ",
      ((Page) valueFromCache.get()).getContent().size() == 3);
    assertTrue("Value should be a page of strings",
      ((Page) valueFromCache.get()).getContent().get(0) instanceof Long);
  }

  @Test
  public void test_get_object_by_key_page_request_and_page_of_entities() {
    List<TestAbstractEntity> valueList = Lists
      .newArrayList(new TestAbstractEntity(1L), new TestAbstractEntity(2L),
        new TestAbstractEntity(3L));

    PageRequest key = new PageRequest(0, 5);
    Page<TestAbstractEntity> value = new PageImpl<>(valueList);
    blossomCache.put(key, value);
    valueList.forEach(v -> blossomCache.put(v.getId().toString(), v));

    ValueWrapper valueFromCache = blossomCache.get(key);
    assertNotNull("Key should return a value", valueFromCache);
    assertNotNull("Key should return a non-null value", valueFromCache.get());
    assertEquals("Key should return the right value", value, valueFromCache.get());
    assertTrue("Value should be a page of 3 elements ",((Page) valueFromCache.get()).getContent().size() == 3);
    assertTrue("Value should be a page of TestAbstractEntity",
      ((Page) valueFromCache.get()).getContent().get(0) instanceof TestAbstractEntity);
  }

  private class TestAbstractEntity extends AbstractEntity {

    public TestAbstractEntity(Long id) {
      this.setId(id);
    }

  }
}
