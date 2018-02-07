package com.blossom_project.ui.web;

import com.blossom_project.ui.menu.OpenedMenu;
import com.blossom_project.ui.stereotype.BlossomController;
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
