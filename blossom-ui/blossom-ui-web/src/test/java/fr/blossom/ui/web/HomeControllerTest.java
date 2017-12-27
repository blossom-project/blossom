package fr.blossom.ui.web;

import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class HomeControllerTest {

  private HomeController controller;

  @Before
  public void setUp() throws Exception {
    this.controller = new HomeController();
  }

  @Test
  public void should_display_home() throws Exception {
    assertTrue(controller.home().getViewName().equals("home/home"));
  }
}
