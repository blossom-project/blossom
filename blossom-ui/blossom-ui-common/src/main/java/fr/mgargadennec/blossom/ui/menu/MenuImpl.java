package fr.mgargadennec.blossom.ui.menu;

import org.springframework.plugin.core.PluginRegistry;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
public class MenuImpl implements Menu {
  private final PluginRegistry<MenuItem, String> registry;

  public MenuImpl(PluginRegistry<MenuItem, String> registry) {
    this.registry = registry;
  }

  @Override
  public Collection<MenuItem> items() {
    return this.registry.getPlugins().stream().filter(item -> item.parent() == null).collect(Collectors.toList());
  }

}
