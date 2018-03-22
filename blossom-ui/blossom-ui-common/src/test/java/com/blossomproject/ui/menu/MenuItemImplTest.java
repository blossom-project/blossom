package com.blossomproject.ui.menu;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.plugin.core.PluginRegistry;

import com.blossomproject.core.common.utils.privilege.Privilege;
import com.blossomproject.core.common.utils.privilege.SimplePrivilege;
import com.blossomproject.ui.current_user.CurrentUser;
import com.blossomproject.ui.current_user.CurrentUserBuilder;
import com.google.common.collect.Lists;

@RunWith(MockitoJUnitRunner.class)
public class MenuItemImplTest {
  @Mock
  PluginRegistry<MenuItem, String> registry;

  @Mock
  MenuItem menuItemParent;

  MenuItem menuItem;

  @Before
  public void setUp() {
    menuItem = new MenuItemImpl(registry, "key1_p", "", "", "", 0, "", true, menuItemParent);
  }

  @Test
  public void should_get_items() {
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("key1_p").label("testLabel1").build();
    MenuItem level1_1 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").parent(level0).build();
    MenuItem level1_2 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").parent(level0).build();
    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1_1, level1_2));

    Collection<MenuItem> items = this.menuItem.items();
    assertTrue(items.size() == 2);
  }

  @Test
  public void should_get_items_with_null_parent() {
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("key1_p").label("testLabel1").build();
    MenuItem level1_1 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").parent(level0).build();
    MenuItem level1_2 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").parent(level0).build();
    MenuItem parent_null = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").parent(null).build();
    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1_1, level1_2, parent_null));

    Collection<MenuItem> items = this.menuItem.items();
    assertTrue(items.size() == 2);
  }

  @Test
  public void should_get_items_with_different_parent() {
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("key1_p").label("testLabel1").build();
    MenuItem level1_1 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").parent(level0).build();
    MenuItem level1_2 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").parent(level0).build();

    MenuItem level0_2 = new MenuItemBuilder(registry, 2).key("key2_p").label("testLabel1").build();
    MenuItem different_parent = new MenuItemBuilder(registry, 2).key("key2_c").label("testLabel1").parent(level0_2)
        .build();

    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1_1, level1_2, different_parent));

    Collection<MenuItem> items = this.menuItem.items();
    assertTrue(items.size() == 2);
  }

  @Test
  public void should_get_root_filtered_items_with_right() throws Exception {
    Privilege privilege = new SimplePrivilege("test:test:test");
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("key1_p").label("testLabel0").privilege(privilege).build();
    MenuItem level1 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").leaf(true).parent(level0)
        .build();
    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1));

    CurrentUser currentUser = new CurrentUserBuilder().identifier("test").passwordHash("test")
        .addPrivilege(privilege.privilege()).toCurrentUser();
    Collection<MenuItem> items = this.menuItem.filteredItems(currentUser);
    assertTrue(items.size() == 1);
  }

  @Test
  public void should_get_root_filtered_items_with_right_but_not_a_leaf() throws Exception {
    Privilege privilege = new SimplePrivilege("test:test:test");
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("key1_p").label("testLabel0").build();
    MenuItem level1 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").leaf(false).parent(level0)
        .privilege(privilege).build();
    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1));

    CurrentUser currentUser = new CurrentUserBuilder().identifier("test").passwordHash("test")
        .addPrivilege(privilege.privilege()).toCurrentUser();
    Collection<MenuItem> items = this.menuItem.filteredItems(currentUser);
    assertTrue(items.isEmpty());
  }

  @Test
  public void should_get_root_filtered_items_without_right() throws Exception {
    Privilege privilege = new SimplePrivilege("test:test:test");
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("key1_p").label("testLabel0").build();
    MenuItem level1 = new MenuItemBuilder(registry, 2).key("key1_c").label("testLabel1").leaf(true).parent(level0)
        .privilege(privilege).build();
    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1));

    CurrentUser currentUser = new CurrentUserBuilder().identifier("test").passwordHash("test").toCurrentUser();
    Collection<MenuItem> items = this.menuItem.filteredItems(currentUser);
    assertTrue(items.isEmpty());
  }

  @Test
  public void should_supports_delimiter() {
    Boolean response = this.menuItem.supports("key1_p");
    assertTrue(response);
  }

  @Test
  public void should_not_supports_delimiter() {
    Boolean response = this.menuItem.supports("blabla");
    assertFalse(response);
  }
}
