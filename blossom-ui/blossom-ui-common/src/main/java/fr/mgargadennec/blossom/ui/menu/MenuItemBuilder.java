package fr.mgargadennec.blossom.ui.menu;

import fr.mgargadennec.blossom.core.common.utils.privilege.Privilege;
import java.util.Collection;
import java.util.stream.Collectors;
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
  private MenuItem parent;

  public MenuItemBuilder(PluginRegistry<MenuItem, String> registry, int maxDepth) {
    this.registry = registry;
    this.maxDepth = maxDepth;
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
    return new MenuItemImpl(registry, key, icon, label, i18n, link, order, privilege, parent);
  }


  /**
   * Created by Maël Gargadennnec on 05/05/2017.
   */
  public static class MenuItemImpl implements MenuItem {

    private final PluginRegistry<MenuItem, String> registry;
    private final String key;
    private final String icon;
    private final String label;
    private final boolean i18n;
    private final String link;
    private final int order;
    private final String privilege;
    private final MenuItem parent;

    MenuItemImpl(PluginRegistry<MenuItem, String> registry, String key, String icon,
      String label, boolean i18n, String link, int order, String privilege,
      MenuItem parent) {
      this.registry = registry;
      this.key = key;
      this.icon = icon;
      this.label = label;
      this.i18n = i18n;
      this.link = link;
      this.order = order;
      this.privilege = privilege;
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
    public boolean i18n() {
      return i18n;
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
    public String privilege(){return privilege;}

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
    public boolean supports(String delimiter) {
      return delimiter.equals(this.key());
    }
  }

}
