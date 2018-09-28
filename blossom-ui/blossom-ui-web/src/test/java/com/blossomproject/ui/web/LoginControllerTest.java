package com.blossomproject.ui.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;;
import org.springframework.ui.Model;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

  private LoginController controller;
  @Mock
  private Model model;
  @Mock
  private HttpServletRequest request;
  @Mock
  private HttpServletResponse response;

  @Mock
  private UriComponentsBuilder builder;
  @Before
  public void setUp() throws Exception {
    this.controller = new LoginController(null, null, null, false);
  }

  @Test
  public void should_display_login_null() throws Exception {
    Optional<String> opt = null;
    ModelAndView modelAndView = controller.getLoginPage(request,response,opt, model);
    assertTrue(modelAndView.getViewName().equals("blossom/login/login"));
    assertTrue(modelAndView.getModel().get("error") == null);
  }

  @Test
  public void should_display_login_empty_optional() throws Exception {
    Optional<String> opt = Optional.empty();
    ModelAndView modelAndView = controller.getLoginPage(request,response,opt, model);
    assertTrue(modelAndView.getViewName().equals("blossom/login/login"));
    assertTrue(modelAndView.getModel().get("error").equals(opt));
  }

  @Test
  public void should_display_login_without_error() throws Exception {
    Optional<String> opt = Optional.of("false");
    ModelAndView modelAndView = controller.getLoginPage(request,response,opt,model);
    assertTrue(modelAndView.getViewName().equals("blossom/login/login"));
    assertTrue(modelAndView.getModel().get("error").equals(opt));
    assertFalse(Boolean.valueOf(((Optional<String>) modelAndView.getModel().get("error")).get()));
  }

  @Test
  public void should_display_login_with_error() throws Exception {
    Optional<String> opt = Optional.of("true");
    ModelAndView modelAndView = controller.getLoginPage(request,response,opt,model);
    assertTrue(modelAndView.getViewName().equals("blossom/login/login"));
    assertTrue(modelAndView.getModel().get("error").equals(opt));
    assertTrue(Boolean.valueOf(((Optional<String>) modelAndView.getModel().get("error")).get()));
  }
}
