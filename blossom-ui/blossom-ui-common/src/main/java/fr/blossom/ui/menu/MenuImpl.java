package fr.blossom.ui.menu;

import com.google.common.base.Preconditions;
import fr.blossom.ui.current_user.CurrentUser;
import java.util.Collection;
import java.util.stream.Collectors;
import org.elasticsearch.common.Strings;
import org.springframework.plugin.core.PluginRegistry;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
public class MenuImpl implements Menu {

  private final PluginRegistry<MenuItem, String> registry;

  public MenuImpl(PluginRegistry<MenuItem, String> registry) {
    Preconditions.checkArgument(registry != null);
    this.registry = registry;
  }

  @Override
  public Collection<MenuItem> items() {
    return this.registry.getPlugins().stream()
      .filter(item -> item.parent() == null)
      .sorted((e1, e2) -> new Integer(e1.order()).compareTo(e2.order()))
      .collect(Collectors.toList());
  }

  @Override
  public Collection<MenuItem> filteredItems(CurrentUser currentUser) {
    return this.registry.getPlugins().stream()
      .filter(item -> item.parent() == null)
      .filter(item -> Strings.isNullOrEmpty(item.privilege()) || currentUser
        .hasPrivilege(item.privilege()))
      .filter(item -> item.leaf() || !item.filteredItems(currentUser).isEmpty())
      .sorted((e1, e2) -> new Integer(e1.order()).compareTo(e2.order()))
      .collect(Collectors.toList());
  }
}
