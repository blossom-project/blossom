package com.blossomproject.ui.menu;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.plugin.core.PluginRegistry;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;

@RunWith(MockitoJUnitRunner.class)
public class MenuInterceptorTest {
  @Mock
  PluginRegistry<MenuItem, String> registry;

  @InjectMocks
  @Spy
  MenuInterceptor interceptor;

  @Test
  public void should_do_nothing_without_handler() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    ModelAndView modelAndView = mock(ModelAndView.class);

    interceptor.postHandle(request, response, null, modelAndView);
    verify(registry, times(0)).getPluginFor(any());
    verify(modelAndView, times(0)).addObject(any(), any());
  }

  @Test
  public void should_do_nothing_without_modelandview() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    Object handler = mock(Object.class);

    interceptor.postHandle(request, response, handler, null);
    verify(registry, times(0)).getPluginFor(any());
  }

  @Test
  public void should_select_home_without_annotation() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HandlerMethod handler = mock(HandlerMethod.class);
    ModelAndView modelAndView = new ModelAndView();

    when(handler.getMethod()).thenReturn(MenuClassWithoutAnnotation.class.getDeclaredMethod("methodWithoutAnnotation"));

    interceptor.postHandle(request, response, handler, modelAndView);

    assertTrue(modelAndView.getModel().containsKey("currentMenu"));
    assertTrue(((List) modelAndView.getModel().get("currentMenu")).contains("home"));

    verify(registry, times(0)).getPluginFor(any());
  }

  @Test
  public void should_select_home_with_annotation_but_without_plugin() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HandlerMethod handler = mock(HandlerMethod.class);
    ModelAndView modelAndView = new ModelAndView();

    when(handler.getMethod()).thenReturn(MenuClassWithoutAnnotation.class.getDeclaredMethod("methodWithAnnotation"));
    when(registry.hasPluginFor(eq("test"))).thenReturn(false);

    interceptor.postHandle(request, response, handler, modelAndView);

    assertTrue(modelAndView.getModel().containsKey("currentMenu"));
    assertTrue(((List) modelAndView.getModel().get("currentMenu")).contains("home"));

    verify(registry, times(0)).getPluginFor(any());
  }

  @Test
  public void should_select_menuitem_with_annotation() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HandlerMethod handler = mock(HandlerMethod.class);
    ModelAndView modelAndView = new ModelAndView();

    MenuItem menuItem = mock(MenuItem.class);
    MenuItem menuItem2 = mock(MenuItem.class);
    when(menuItem.key()).thenReturn("key_menu_1");
    when(menuItem.parent()).thenReturn(menuItem2);
    when(menuItem2.key()).thenReturn("key_menu_2");

    when(handler.getMethod()).thenReturn(MenuClassWithoutAnnotation.class.getDeclaredMethod("methodWithAnnotation"));
    when(registry.hasPluginFor(eq("test"))).thenReturn(true);
    when(registry.getPluginFor(eq("test"))).thenReturn(menuItem);

    interceptor.postHandle(request, response, handler, modelAndView);

    assertTrue(modelAndView.getModel().containsKey("currentMenu"));
    assertTrue(((List) modelAndView.getModel().get("currentMenu")).contains("key_menu_1"));
    assertTrue(((List) modelAndView.getModel().get("currentMenu")).contains("key_menu_2"));
    assertEquals(2, ((List) modelAndView.getModel().get("currentMenu")).size());

    verify(registry, times(1)).getPluginFor(any());
  }

  @Test
  public void should_select_menuitem_with_annotation_in_declaring_class() throws Exception {
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    HandlerMethod handler = mock(HandlerMethod.class);
    ModelAndView modelAndView = new ModelAndView();

    MenuItem menuItem = mock(MenuItem.class);
    MenuItem menuItem2 = mock(MenuItem.class);
    when(menuItem.key()).thenReturn("key_menu_1");
    when(menuItem.parent()).thenReturn(menuItem2);
    when(menuItem2.key()).thenReturn("key_menu_2");

    when(handler.getMethod()).thenReturn(MenuClassWithAnnotation.class.getDeclaredMethod("methodWithoutAnnotation"));
    when(registry.hasPluginFor(eq("test"))).thenReturn(true);
    when(registry.getPluginFor(eq("test"))).thenReturn(menuItem);

    interceptor.postHandle(request, response, handler, modelAndView);

    assertTrue(modelAndView.getModel().containsKey("currentMenu"));
    assertTrue(((List) modelAndView.getModel().get("currentMenu")).contains("key_menu_1"));
    assertTrue(((List) modelAndView.getModel().get("currentMenu")).contains("key_menu_2"));
    assertEquals(2, ((List) modelAndView.getModel().get("currentMenu")).size());

    verify(registry, times(1)).getPluginFor(any());
  }

  public class MenuClassWithoutAnnotation {
    public void methodWithoutAnnotation() {
    }

    @OpenedMenu("test")
    public void methodWithAnnotation() {
    }
  }

  @OpenedMenu("test")
  public class MenuClassWithAnnotation {
    public void methodWithoutAnnotation() {
    }
  }
}
