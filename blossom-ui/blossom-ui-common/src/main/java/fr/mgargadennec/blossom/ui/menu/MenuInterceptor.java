package fr.mgargadennec.blossom.ui.menu;

import com.google.common.collect.Lists;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.List;

/**
 * Created by Maël Gargadennnec on 08/06/2017.
 */
public class MenuInterceptor extends HandlerInterceptorAdapter {

  private final PluginRegistry<MenuItem, String> registry;

  public MenuInterceptor(PluginRegistry<MenuItem, String> registry) {
    this.registry = registry;
  }

  @Override
  public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    if (handler != null && modelAndView != null) {
      HandlerMethod hm = (HandlerMethod) handler;
      Method method = hm.getMethod();

      String menuId = null;
      if (method.isAnnotationPresent(OpenedMenu.class)) {
        menuId = method.getAnnotation(OpenedMenu.class).value();
      } else if (method.getDeclaringClass().isAnnotationPresent(OpenedMenu.class)) {
        menuId = method.getDeclaringClass().getAnnotation(OpenedMenu.class).value();
      }

      if (!StringUtils.isEmpty(menuId) && registry.hasPluginFor(menuId)) {
        List<String> currentMenu = Lists.newArrayList();
        MenuItem menuItem = registry.getPluginFor(menuId);
        while (menuItem != null) {
          currentMenu.add(menuItem.key());
          menuItem = menuItem.parent();
        }

        modelAndView.addObject("currentMenu", currentMenu);
      }
    }
  }
}
