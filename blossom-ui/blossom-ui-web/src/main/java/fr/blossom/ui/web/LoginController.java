package fr.blossom.ui.web;

import fr.blossom.ui.stereotype.BlossomController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@BlossomController
@RequestMapping("/login")
public class LoginController {

  @GetMapping
  public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
    return new ModelAndView("blossom/login/login", "error", error);
  }

}
