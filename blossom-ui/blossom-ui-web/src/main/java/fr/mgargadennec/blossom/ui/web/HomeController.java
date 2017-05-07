package fr.mgargadennec.blossom.ui.web;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@BlossomController
public class HomeController {

  @GetMapping
  public ModelAndView home() {
    return new ModelAndView("home/home");
  }
}
