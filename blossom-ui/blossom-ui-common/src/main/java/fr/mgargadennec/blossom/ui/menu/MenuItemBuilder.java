package fr.mgargadennec.blossom.ui.menu;

import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import org.springframework.plugin.core.PluginRegistry;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
public class MenuItemBuilder {

  private final PluginRegistry<MenuItem, String> registry;
  private final int maxDepth;
  private String key;
  private String icon;
  private String label;
  private boolean i18n;
  private String link;
  private int order;
  private String privilege;
  private boolean leaf;
  private MenuItem parent;

  public MenuItemBuilder(PluginRegistry<MenuItem, String> registry, int maxDepth) {
    this.registry = registry;
    this.maxDepth = maxDepth;
    this.leaf = true;
  }

  public MenuItemBuilder key(String key) {
    this.key = key;
    return this;
  }

  public MenuItemBuilder icon(String icon) {
    this.icon = icon;
    return this;
  }

  public MenuItemBuilder label(String label, boolean i18n) {
    this.label = label;
    this.i18n = i18n;
    return this;
  }

  public MenuItemBuilder link(String link) {
    this.link = link;
    return this;
  }

  public MenuItemBuilder order(int order) {
    this.order = order;
    return this;
  }

  public MenuItemBuilder privilege(Privilege privilege) {
    this.privilege = privilege.privilege();
    return this;
  }

  public MenuItemBuilder leaf(boolean leaf) {
    this.leaf = leaf;
    return this;
  }


  public MenuItemBuilder parent(MenuItem parent) {
    this.parent = parent;
    checkLevel();
    return this;
  }

  private void checkLevel() {
    if (this.parent != null && this.parent.level() >= maxDepth) {
      throw new RuntimeException(
        "Cannot add MenuItem to parent (key = " + parent.key() + ", level=" + parent.level()
          + ") because max depth is " + maxDepth);
    }
  }


  public MenuItem build() {
    return new MenuItemImpl(registry, key, icon, label, i18n, link, order, privilege, leaf, parent);
  }

}
