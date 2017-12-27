package fr.blossom.ui.menu;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Lists;
import fr.blossom.core.common.utils.privilege.Privilege;
import fr.blossom.core.common.utils.privilege.SimplePrivilege;
import fr.blossom.ui.current_user.CurrentUser;
import fr.blossom.ui.current_user.CurrentUserBuilder;
import java.util.Collection;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.plugin.core.PluginRegistry;

@RunWith(MockitoJUnitRunner.class)
public class MenuImplTest {

  @Rule
  public ExpectedException thrown = ExpectedException.none();
  private PluginRegistry<MenuItem, String> registry;
  private MenuImpl menu;

  @Before
  public void setUp() throws Exception {
    this.registry = mock(PluginRegistry.class);
    this.menu = new MenuImpl(this.registry);
  }

  @Test
  public void should_fail_on_null_registry() {
    thrown.expect(IllegalArgumentException.class);
    new MenuImpl(null);
  }

  @Test
  public void should_get_root_items() throws Exception {
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("level0").label("testLabel0").build();
    MenuItem level1 = new MenuItemBuilder(registry, 2).key("level1").label("testLabel1")
      .parent(level0).build();
    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1));

    Collection<MenuItem> items = this.menu.items();
    assertTrue(items.size() == 1);
  }

  @Test
  public void should_get_root_filtered_items_with_right() throws Exception {
    Privilege privilege = new SimplePrivilege("test:test:test");
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("level0").label("testLabel0").privilege(privilege).build();
    MenuItem level1 = new MenuItemBuilder(registry, 2).key("level1").label("testLabel1")
      .parent(level0).build();
    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1));

    CurrentUser currentUser = new CurrentUserBuilder().identifier("test").passwordHash("test").addPrivilege(privilege.privilege()).toCurrentUser();
    Collection<MenuItem> items = this.menu.filteredItems(currentUser);
    assertTrue(items.size() == 1);
  }

  @Test
  public void should_get_root_filtered_items_without_right() throws Exception {
    Privilege privilege = new SimplePrivilege("test:test:test");
    MenuItem level0 = new MenuItemBuilder(registry, 2).key("level0").label("testLabel0").privilege(privilege).build();
    MenuItem level1 = new MenuItemBuilder(registry, 2).key("level1").label("testLabel1")
      .parent(level0).build();
    when(this.registry.getPlugins()).thenReturn(Lists.newArrayList(level0, level1));

    CurrentUser currentUser = new CurrentUserBuilder().identifier("test").passwordHash("test").toCurrentUser();
    Collection<MenuItem> items = this.menu.filteredItems(currentUser);
    assertTrue(items.size() == 0);
  }
}
