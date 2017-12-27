package fr.blossom.ui.menu;

import com.google.common.base.Preconditions;
import fr.blossom.core.common.utils.privilege.Privilege;
import org.springframework.plugin.core.PluginRegistry;

/**
 * Helper class for easily creating {@code MenuItem}s.<br/> It relies on a {@code
 * PluginRegistry<MenuItem,String>} that holds other {@link MenuItem} to find them by their
 * identifier.
 *
 * @author Maël Gargadennnec
 */
public class MenuItemBuilder {

  private final PluginRegistry<MenuItem, String> registry;
  private final int maxDepth;
  private String key;
  private String icon;
  private String label;
  private String link;
  private String privilege;
  private MenuItem parent;
  private int order = Integer.MAX_VALUE;
  private boolean leaf = true;

  /**
   * Builds a new {@code MenuItemBuilder} working with a given {@code PluginRegistry<MenuItem,
   * String>}  registry.</br> Initialize the menu to be a leaf by default.
   *
   * @param registry the registry the resulting {@link MenuItem} should be a part of. If null,
   * throws an {@code IllegalArgumentException}
   * @param maxDepth the maximum depth of nested menus. Throws an {@code IllegalArgumentException}
   * if negative value is passed.
   */
  public MenuItemBuilder(PluginRegistry<MenuItem, String> registry, int maxDepth) {
    Preconditions.checkArgument(registry != null);
    Preconditions.checkArgument(maxDepth >= 0);
    this.registry = registry;
    this.maxDepth = maxDepth;
  }

  /**
   * Mandatory identifier for the constructed {@link MenuItem}
   *
   * @param key the unique identifier of the constructed {@code MenuItem}
   * @return {@literal this}
   */
  public MenuItemBuilder key(String key) {
    this.key = key;
    return this;
  }

  /**
   * Mandatory label for the constructed {@link MenuItem}<br/> Can be an i18n key.
   *
   * @param label the label or i18n key
   * @return {@literal this}
   */
  public MenuItemBuilder label(String label) {
    this.label = label;
    return this;
  }


  /**
   * Facultative icon class
   *
   * @param icon the icon class
   * @return {@literal this}
   */
  public MenuItemBuilder icon(String icon) {
    this.icon = icon;
    return this;
  }


  /**
   * Facultative link
   *
   * @param link a URI/URL pointing to a location for this menu item
   * @return {@literal this}
   */
  public MenuItemBuilder link(String link) {
    this.link = link;
    return this;
  }

  /**
   * Facultative order relative to its same-level peer {@code MenuItem}<br/>
   *
   * Min priority : Integer.MIN_VALUE<br/> Max priority : Integer.MAX_VALUE<br/> Default priority :
   * Integer.MIN_VALUE<br/>
   *
   * @param order the expected order
   * @return {@literal this}
   */
  public MenuItemBuilder order(int order) {
    this.order = order;
    return this;
  }

  /**
   * Facultative {@link Privilege} on the menu.<br/>
   *
   * Privileges on the {@link Menu} will be used to filter {@link MenuItem}s depending on the {@link
   * fr.blossom.ui.current_user.CurrentUser}
   *
   * @param privilege the privilege to set
   * @return {@literal this}
   */
  public MenuItemBuilder privilege(Privilege privilege) {
    this.privilege = privilege == null ? null : privilege.privilege();
    return this;
  }

  /**
   * Facultative leaf parameter : indicates whether or not a {@link MenuItem} should be displayed
   * when it has no visible children.<br/> Leaves will always be displayed, whereas not-leaves
   * without displayed children will not be.
   *
   * @param leaf the property to set
   * @return {@literal this}
   */
  public MenuItemBuilder leaf(boolean leaf) {
    this.leaf = leaf;
    return this;
  }


  /**
   * Set the parent {@code MenuItem} to the current one.<br/> Throws an {@code
   * IllegalStateException} if the parent's level is already at {@link this#maxDepth}
   *
   * @param parent the parent menuItem
   * @return {@literal this}
   */
  public MenuItemBuilder parent(MenuItem parent) {
    this.parent = parent;
    this.checkLevel();
    return this;
  }

  /**
   * Check the parent level validity
   */
  private void checkLevel() {
    if (this.parent != null && this.parent.level() >= maxDepth) {
      throw new IllegalStateException(
        "Cannot add MenuItem to parent (key = " + parent.key() + ", level=" + parent.level()
          + ") because max depth is " + maxDepth);
    }
  }


  /**
   * Check mandatory fields then builds the {@code MenuItem} with the current state of the {@code
   * MenuItemBuilder}</br>
   *
   * @return the newly built {@link MenuItemImpl}
   */
  public MenuItem build() {
    Preconditions.checkState(this.key != null);
    Preconditions.checkState(this.label != null);
    return new MenuItemImpl(registry, key, icon, label, link, order, privilege, leaf, parent);
  }

}
