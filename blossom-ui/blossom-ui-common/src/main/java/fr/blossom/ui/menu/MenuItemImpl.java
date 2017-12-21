package fr.blossom.ui.menu;

import fr.blossom.ui.current_user.CurrentUser;
import java.util.Collection;
import java.util.stream.Collectors;
import org.elasticsearch.common.Strings;
import org.springframework.plugin.core.PluginRegistry;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
public class MenuItemImpl implements MenuItem {

  private final PluginRegistry<MenuItem, String> registry;
  private final String key;
  private final String icon;
  private final String label;
  private final String link;
  private final int order;
  private final String privilege;
  private final boolean leaf;
  private final MenuItem parent;

  MenuItemImpl(PluginRegistry<MenuItem, String> registry, String key, String icon,
    String label, String link, int order, String privilege, boolean leaf,
    MenuItem parent) {
    this.registry = registry;
    this.key = key;
    this.icon = icon;
    this.label = label;
    this.link = link;
    this.order = order;
    this.privilege = privilege;
    this.leaf = leaf;
    this.parent = parent;
  }


  @Override
  public String key() {
    return key;
  }

  @Override
  public String icon() {
    return icon;
  }

  @Override
  public String label() {
    return label;
  }

  @Override
  public String link() {
    return link;
  }

  @Override
  public int order() {
    return order;
  }

  @Override
  public int level() {
    return this.parent() == null ? 1 : this.parent().level() + 1;
  }

  @Override
  public String privilege() {
    return privilege;
  }

  @Override
  public boolean leaf() {
    return leaf;
  }

  @Override
  public MenuItem parent() {
    return parent;
  }

  @Override
  public Collection<MenuItem> items() {
    return this.registry.getPlugins().stream()
      .filter(item -> item.parent() != null && item.parent().key().equals(this.key))
      .sorted((e1, e2) -> new Integer(e1.order()).compareTo(e2.order()))
      .collect(Collectors.toList());
  }

  @Override
  public Collection<MenuItem> filteredItems(CurrentUser currentUser) {
    return this.registry.getPlugins().stream()
      .filter(item -> item.parent() != null && item.parent().key().equals(this.key))
      .filter(item-> Strings.isNullOrEmpty(item.privilege()) || currentUser.hasPrivilege(item.privilege()))
      .filter(item -> item.leaf() || !item.filteredItems(currentUser).isEmpty())
      .sorted((e1, e2) -> new Integer(e1.order()).compareTo(e2.order()))
      .collect(Collectors.toList());
  }

  @Override
  public boolean supports(String delimiter) {
    return delimiter.equals(this.key());
  }
}
