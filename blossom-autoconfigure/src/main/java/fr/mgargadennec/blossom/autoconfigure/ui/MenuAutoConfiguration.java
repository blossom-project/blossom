package fr.mgargadennec.blossom.autoconfigure.ui;

import fr.mgargadennec.blossom.core.common.PluginConstants;
import fr.mgargadennec.blossom.ui.menu.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@Configuration
@EnablePluginRegistries({MenuItemPlugin.class})
public class MenuAutoConfiguration {
  @Autowired
  @Qualifier(value = PluginConstants.PLUGIN_MENU)
  private PluginRegistry<MenuItem, String> registry;

  @Bean
  @Scope(value = BeanDefinition.SCOPE_PROTOTYPE)
  public MenuItemBuilder menuItemBuilder() {
    return new MenuItemBuilder(registry, 2);
  }

  @Bean
  public Menu menu() {
    return new MenuImpl(registry);
  }

  @Bean
  @Order(0)
  public MenuItem homeMenuItem(MenuItemBuilder builder) {
    return builder.key("home").label("menu.home", true).icon("fa fa-home").link("/blossom/system").build();
  }

  @Bean
  @Order(0)
  public MenuItem administrationMenuItem(MenuItemBuilder builder) {
    return builder.key("administration").label("menu.administration", true).icon("glyphicon glyphicon-list-alt").link("/blossom/administration").build();
  }

  @Bean
  @Order(0)
  public MenuItem systemMenuItem(MenuItemBuilder builder) {
    return builder.key("system").label("menu.system", true).icon("fa fa-cogs").link("/blossom/system").build();
  }


}
