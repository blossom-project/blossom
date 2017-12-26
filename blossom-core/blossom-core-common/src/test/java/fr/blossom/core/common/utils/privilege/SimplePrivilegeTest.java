package fr.blossom.core.common.utils.privilege;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SimplePrivilegeTest {
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void should_init_with_string() {
    String privilege = "test1:test2:test3";
    SimplePrivilege simplePrivilege = new SimplePrivilege(privilege);
    assertEquals(simplePrivilege.namespace(), "test1");
    assertEquals(simplePrivilege.feature(), "test2");
    assertEquals(simplePrivilege.right(), "test3");
    assertEquals(simplePrivilege.privilege(), privilege);
  }

  @Test
  public void should_init_with_null_string() {
    thrown.expect(IllegalArgumentException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege(null);
  }
  @Test
  public void should_init_with_empty_string() {
    thrown.expect(IllegalArgumentException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege("");
  }
  @Test
  public void should_init_with_malformed_string() {
    thrown.expect(IllegalStateException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege("testtesttest");
  }


  @Test
  public void should_init_with_args() {
    SimplePrivilege simplePrivilege = new SimplePrivilege("test1", "test2","test3");
    assertEquals(simplePrivilege.namespace(), "test1");
    assertEquals(simplePrivilege.feature(), "test2");
    assertEquals(simplePrivilege.right(), "test3");
    assertEquals(simplePrivilege.privilege(), "test1:test2:test3");
  }

  @Test
  public void should_init_with_null_namespace_arg() {
    thrown.expect(IllegalArgumentException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege(null, "test2", "test3");
  }

  @Test
  public void should_init_with_null_feature_arg() {
    thrown.expect(IllegalArgumentException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege("test1", null, "test3");
  }

  @Test
  public void should_init_with_null_right_arg() {
    thrown.expect(IllegalArgumentException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege("test1", "test2", null);
  }

  @Test
  public void should_init_with_empty_namespace_arg() {
    thrown.expect(IllegalArgumentException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege("", "test2", "test3");
  }

  @Test
  public void should_init_with_empty_feature_arg() {
    thrown.expect(IllegalArgumentException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege("test1", "", "test3");
  }

  @Test
  public void should_init_with_empty_right_arg() {
    thrown.expect(IllegalArgumentException.class);
    SimplePrivilege simplePrivilege = new SimplePrivilege("test1", "test2", "");
  }

  @Test
  public void should_plugin_support_privilege(){
    SimplePrivilege simplePrivilege = new SimplePrivilege("test1", "test2", "test3");
    assertTrue(simplePrivilege.supports("test1:test2:test3"));
  }
}
