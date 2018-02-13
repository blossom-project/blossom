package com.blossomproject.ui.web;

import com.blossomproject.ui.menu.OpenedMenu;
import com.blossomproject.ui.stereotype.BlossomController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@BlossomController
@OpenedMenu("home")
public class HomeController {

  @RequestMapping
  public ModelAndView home() {
    return new ModelAndView("blossom/home/home");
  }

}
