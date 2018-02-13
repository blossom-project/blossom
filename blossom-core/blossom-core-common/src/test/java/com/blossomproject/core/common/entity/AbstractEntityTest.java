package com.blossomproject.core.common.entity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.spy;

import java.util.Date;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;

@RunWith(MockitoJUnitRunner.class)
public class AbstractEntityTest {

  @Test
  public void should_ensure_id_be_set_if_not_already_set() throws Exception {
    AbstractEntity test = spy(new TestAbstractEntity());
    test.ensureId();
    assertNotNull(test.getId());
  }

  @Test
  public void should_ensure_id_not_change_id_if_already_set() throws Exception {
    Long id = 12345L;
    AbstractEntity test = spy(new TestAbstractEntity());
    test.setId(id);
    test.ensureId();
    assertNotNull(test.getId());
    assertEquals(test.getId(), id);
  }


  @Test
  public void should_be_equals_if_id_are_equals() throws Exception {
    Long id = 12345L;
    AbstractEntity o1 = new TestAbstractEntity();
    o1.setId(id);
    AbstractEntity o2 = new TestAbstractEntity();
    o2.setId(id);

    assertEquals(o1.hashCode(), o2.hashCode());
    assertTrue(o1.equals(o2));
    assertTrue(o2.equals(o1));
  }

  @Test
  public void should_be_equals_if_id_are_equals_not_regarding_other_fields() throws Exception {
    Long id = 12345L;
    AbstractEntity o1 = new TestAbstractEntity();
    o1.setId(id);
    o1.setCreationDate(new Date());
    o1.setModificationDate(new Date());
    o1.setCreationUser("test");
    o1.setModificationUser("test");
    AbstractEntity o2 = new TestAbstractEntity();
    o2.setId(id);
    o2.setCreationDate(new Date(System.currentTimeMillis()+15000L));
    o2.setModificationDate(new Date(System.currentTimeMillis()+15000L));
    o2.setCreationUser("test");
    o2.setModificationUser("test");

    assertEquals(o1.hashCode(), o2.hashCode());
    assertTrue(o1.equals(o2));
    assertTrue(o2.equals(o1));
    assertTrue(o1.getCreationDate().before(o2.getCreationDate()));
    assertTrue(o1.getModificationDate().before(o2.getModificationDate()));
    assertEquals(o1.getCreationUser(), o2.getCreationUser());
    assertEquals(o1.getModificationUser(), o2.getModificationUser());
  }

  @Test
  public void should_not_be_equals_if_id_are_not_equals() throws Exception {
    AbstractEntity o1 = new TestAbstractEntity();
    o1.setId(1L);
    AbstractEntity o2 = new TestAbstractEntity();
    o2.setId(2L);

    assertNotEquals(o1.hashCode(), o2.hashCode());
    assertFalse(o1.equals(o2));
    assertFalse(o2.equals(o1));
  }

  @Test
  public void should_not_be_equals_if_id_are_nulls() throws Exception {
    AbstractEntity o1 = new TestAbstractEntity();
    AbstractEntity o2 = new TestAbstractEntity();

    assertEquals(o1.hashCode(), o2.hashCode());
    assertFalse(o1.equals(o2));
  }

  @Test
  public void should_not_be_equals_if_classes_are_different() throws Exception {
    AbstractEntity o1 = new TestAbstractEntity();
    o1.setId(1L);
    AbstractEntity o2 = new TestAbstractEntity2();
    o2.setId(1L);

    assertEquals(o1.hashCode(), o2.hashCode());
    assertFalse(o1.equals(o2));
  }


  @Test
  public void should_not_be_equals_if_other_is_null() throws Exception {
    AbstractEntity o1 = new TestAbstractEntity();
    o1.setId(1L);
    assertFalse(o1.equals(null));
  }


  private class TestAbstractEntity extends AbstractEntity {

  }

  private class TestAbstractEntity2 extends AbstractEntity {

  }
}
