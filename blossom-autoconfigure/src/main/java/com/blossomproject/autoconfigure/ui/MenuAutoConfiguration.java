package com.blossomproject.autoconfigure.ui;

import static com.blossomproject.autoconfigure.ui.WebContextAutoConfiguration.BLOSSOM_BASE_PATH;

import com.blossomproject.core.common.PluginConstants;
import com.blossomproject.ui.menu.Menu;
import com.blossomproject.ui.menu.MenuImpl;
import com.blossomproject.ui.menu.MenuInterceptor;
import com.blossomproject.ui.menu.MenuItem;
import com.blossomproject.ui.menu.MenuItemBuilder;
import com.blossomproject.ui.menu.MenuItemPlugin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.annotation.Order;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.plugin.core.config.EnablePluginRegistries;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Created by Maël Gargadennnec on 05/05/2017.
 */
@Configuration
@EnablePluginRegistries({MenuItemPlugin.class})
@ConditionalOnWebApplication
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
  public MenuItem homeMenuItem(MenuItemBuilder menuItemBuilder) {
    return menuItemBuilder
      .key("home")
      .label("menu.home")
      .icon("fa fa-home")
      .link("/blossom")
      .order(Integer.MIN_VALUE)
      .build();
  }

  @Bean
  @Order(0)
  public MenuItem administrationMenuItem(MenuItemBuilder menuItemBuilder) {
    return menuItemBuilder.key("administration")
      .label("menu.administration")
      .icon("glyphicon glyphicon-list-alt")
      .link("/blossom/administration")
      .leaf(false)
      .order(Integer.MIN_VALUE + 1)
      .build();
  }

  @Bean
  @Order(0)
  public MenuItem systemMenuItem(MenuItemBuilder menuItemBuilder) {
    return menuItemBuilder
      .key("system")
      .label("menu.system")
      .icon("fa fa-cogs")
      .link("/blossom/system")
      .leaf(false)
      .order(Integer.MAX_VALUE)
      .build();
  }


  @Configuration
  public static class MenuWebAutoConfiguration  implements WebMvcConfigurer{

    @Autowired
    @Qualifier(value = PluginConstants.PLUGIN_MENU)
    private PluginRegistry<MenuItem, String> registry;

    @Bean
    public MenuInterceptor menuInterceptor() {
      return new MenuInterceptor(registry);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
      registry.addInterceptor(menuInterceptor()).addPathPatterns("/" + BLOSSOM_BASE_PATH + "/**");
    }
  }

}
