package fr.blossom.ui.web;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Optional;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.web.servlet.ModelAndView;

@RunWith(MockitoJUnitRunner.class)
public class LoginControllerTest {

  private LoginController controller;

  @Before
  public void setUp() throws Exception {
    this.controller = new LoginController();
  }

  @Test
  public void should_display_login_null() throws Exception {
    Optional<String> opt = null;
    ModelAndView modelAndView = controller.getLoginPage(opt);
    assertTrue(modelAndView.getViewName().equals("login/login"));
    assertTrue(modelAndView.getModel().get("error") == null);
  }

  @Test
  public void should_display_login_empty_optional() throws Exception {
    Optional<String> opt = Optional.empty();
    ModelAndView modelAndView = controller.getLoginPage(opt);
    assertTrue(modelAndView.getViewName().equals("login/login"));
    assertTrue(modelAndView.getModel().get("error").equals(opt));
  }

  @Test
  public void should_display_login_without_error() throws Exception {
    Optional<String> opt = Optional.of("false");
    ModelAndView modelAndView = controller.getLoginPage(opt);
    assertTrue(modelAndView.getViewName().equals("login/login"));
    assertTrue(modelAndView.getModel().get("error").equals(opt));
    assertFalse(Boolean.valueOf(((Optional<String>) modelAndView.getModel().get("error")).get()));
  }

  @Test
  public void should_display_login_with_error() throws Exception {
    Optional<String> opt = Optional.of("true");
    ModelAndView modelAndView = controller.getLoginPage(opt);
    assertTrue(modelAndView.getViewName().equals("login/login"));
    assertTrue(modelAndView.getModel().get("error").equals(opt));
    assertTrue(Boolean.valueOf(((Optional<String>) modelAndView.getModel().get("error")).get()));
  }
}
