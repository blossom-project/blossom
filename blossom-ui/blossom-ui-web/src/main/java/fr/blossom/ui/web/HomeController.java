package fr.blossom.ui.web;

import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@BlossomController
@OpenedMenu("home")
public class HomeController {

  @RequestMapping
  public ModelAndView home() {
    return new ModelAndView("home/home");
  }

}
