package com.blossomproject.ui.theme;

import static org.springframework.web.servlet.support.RequestContext.DEFAULT_THEME_NAME;

import com.blossomproject.ui.stereotype.BlossomController;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.ThemeResolver;

@ControllerAdvice(annotations = BlossomController.class)
public class ThemeControllerAdvice {
  private final PluginRegistry<Theme, String> themeRegistry;
  private final ThemeResolver themeResolver;

  public ThemeControllerAdvice(PluginRegistry<Theme, String> themeRegistry,
    ThemeResolver themeResolver) {
    this.themeRegistry=themeRegistry;
    this.themeResolver = themeResolver;
  }

  @ModelAttribute("themes")
  public List<Theme> themes() {
    return themeRegistry.getPlugins();
  }

  @ModelAttribute("currentTheme")
  public Theme currentTheme(HttpServletRequest request) {
    return themeRegistry.getPluginFor(themeResolver.resolveThemeName(request), themeRegistry.getPluginFor(DEFAULT_THEME_NAME));
  }

}
