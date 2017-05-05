package fr.mgargadennec.blossom.ui.web;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Optional;

@BlossomController("/login")
public class LoginController {

  @GetMapping
  public ModelAndView getLoginPage(@RequestParam Optional<String> error) {
    return new ModelAndView("login/login", "error", error);
  }

}
