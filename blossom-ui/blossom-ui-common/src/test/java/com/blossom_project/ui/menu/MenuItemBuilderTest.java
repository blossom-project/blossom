package com.blossom_project.ui.menu;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.blossom_project.core.common.utils.privilege.Privilege;
import com.blossom_project.core.common.utils.privilege.SimplePrivilege;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.plugin.core.PluginRegistry;

@RunWith(MockitoJUnitRunner.class)
public class MenuItemBuilderTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  public PluginRegistry<MenuItem, String> registry;

  @Before
  public void setUp() throws Exception {
    this.registry = mock(PluginRegistry.class);
  }

  @Test
  public void should_create_builder_null_registry_with_failure() {
    thrown.expect(IllegalArgumentException.class);
    new MenuItemBuilder(null, 5);
  }

  @Test
  public void should_create_builder_negative_max_depth_with_failure() {
    thrown.expect(IllegalArgumentException.class);
    new MenuItemBuilder(this.registry, -1);
  }

  @Test
  public void should_create_builder_zero_max_depth_with_success() {
    new MenuItemBuilder(this.registry, 0);
  }

  @Test
  public void should_build_with_success() {
    String key = "testkey";
    String label = "testlabel";
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 0).key(key)
      .label(label);
    assertEquals(key, menuItemBuilder.build().key());
    assertEquals(label, menuItemBuilder.build().label());
  }

  @Test
  public void should_build_key_null() {
    thrown.expect(IllegalStateException.class);
    new MenuItemBuilder(this.registry, 0).key(null).build();
  }

  @Test
  public void should_build_key_not_set() {
    thrown.expect(IllegalStateException.class);
    new MenuItemBuilder(this.registry, 0).build();
  }

  @Test
  public void should_build_label_with_success() {
    String label = "testLabel";
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 0)
      .key("test")
      .label(label);
    assertEquals(label, menuItemBuilder.build().label());
  }

  @Test
  public void should_build_label_null() {
    thrown.expect(IllegalStateException.class);
    new MenuItemBuilder(this.registry, 0).key("test").label(null).build();
  }

  @Test
  public void should_build_label_not_set() {
    thrown.expect(IllegalStateException.class);
    new MenuItemBuilder(this.registry, 0).key("test").build();
  }

  @Test
  public void should_build_icon_with_success() {
    String icon = "testIcon";
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 0)
      .key("key")
      .label("label")
      .icon(icon);
    assertEquals(icon, menuItemBuilder.build().icon());
  }

  @Test
  public void should_build_icon_null_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0).key("test").label("test")
      .icon(null).build();
    assertNull(menuItem.icon());
  }

  @Test
  public void should_build_icon_not_set_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0).key("test").label("test")
      .build();
    assertNull(menuItem.icon());
  }

  @Test
  public void should_build_link_with_success() {
    String link = "testLink";
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 0)
      .key("key")
      .label("label")
      .link(link);
    assertEquals(link, menuItemBuilder.build().link());
  }

  @Test
  public void should_build_link_null_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0)
      .key("test").label("test")
      .link(null).build();
    assertNull(menuItem.link());
  }

  @Test
  public void should_build_link_not_set_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0)
      .key("test").label("test").build();
    assertNull(menuItem.link());
  }

  @Test
  public void should_build_order_with_success() {
    int order = 5;
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 0)
      .key("key")
      .label("label")
      .order(order);
    assertEquals(order, menuItemBuilder.build().order());
  }

  @Test
  public void should_build_order_null_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0)
      .key("test").label("test")
      .build();
    assertNotNull(menuItem.order());
    assertEquals(Integer.MAX_VALUE, menuItem.order());
  }


  @Test
  public void should_build_privilege_with_success() {
    Privilege privilege = new SimplePrivilege("test:test:test");
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 0)
      .key("key")
      .label("label")
      .privilege(privilege);
    assertEquals(privilege.privilege(), menuItemBuilder.build().privilege());
  }

  @Test
  public void should_build_privilege_null_with_failure() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0)
      .key("test").label("test")
      .privilege(null)
      .build();
    assertNull(menuItem.privilege());
  }

  @Test
  public void should_build_privilege_not_set_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0)
      .key("test").label("test").build();
    assertNull(menuItem.privilege());
  }


  @Test
  public void should_build_leaf_true_with_success() {
    boolean leaf = true;
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 0)
      .key("key")
      .label("label")
      .leaf(leaf);
    assertEquals(leaf, menuItemBuilder.build().leaf());
  }

  @Test
  public void should_build_leaf_false_with_success() {
    boolean leaf = false;
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 0)
      .key("key")
      .label("label")
      .leaf(leaf);
    assertEquals(leaf, menuItemBuilder.build().leaf());
  }

  @Test
  public void should_build_leaf_null_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0)
      .key("test").label("test")
      .build();
    assertNotNull(menuItem.leaf());
    assertTrue(menuItem.leaf());
  }

  @Test
  public void should_build_parent_with_success() {
    MenuItem parent = mock(MenuItem.class);
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 2)
      .key("key")
      .label("label")
      .parent(parent);
    assertEquals(parent, menuItemBuilder.build().parent());
  }

  @Test
  public void should_build_parent_max_depth_with_success() {
    MenuItem parent = mock(MenuItem.class);
    when(parent.level()).thenReturn(0);
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, 2)
      .key("key")
      .label("label")
      .parent(parent);
    assertEquals(parent, menuItemBuilder.build().parent());
  }


  @Test
  public void should_build_parent_max_depth_with_failure() {
    thrown.expect(IllegalStateException.class);
    int maxdepth = 2;
    MenuItem parent = mock(MenuItem.class);
    when(parent.key()).thenReturn("parentKey");
    when(parent.level()).thenReturn(maxdepth);
    MenuItemBuilder menuItemBuilder = new MenuItemBuilder(this.registry, maxdepth)
      .key("key")
      .label("label")
      .parent(parent);
  }

  @Test
  public void should_build_parent_null_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0)
      .key("test").label("test")
      .parent(null)
      .build();
    assertNull(menuItem.parent());
  }

  @Test
  public void should_build_parent_not_set_with_success() {
    MenuItem menuItem = new MenuItemBuilder(this.registry, 0)
      .key("test").label("test").build();
    assertNull(menuItem.parent());
  }

}
