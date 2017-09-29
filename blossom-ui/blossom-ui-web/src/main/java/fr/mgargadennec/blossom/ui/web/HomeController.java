package fr.mgargadennec.blossom.ui.web;

import fr.mgargadennec.blossom.ui.current_user.CurrentUser;
import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@BlossomController
@OpenedMenu("home")
public class HomeController {

  @RequestMapping
  public ModelAndView home() {
    return new ModelAndView("home/home");
  }

  @Autowired
  public SessionRegistry sessionRegistry;

  @GetMapping("/currents")
  @ResponseBody
  public List<CurrentUser> currentUsers(){
    List<CurrentUser> list =sessionRegistry.getAllPrincipals().stream()
      .filter(u -> !sessionRegistry.getAllSessions(u, false).isEmpty())
      .map( p -> (CurrentUser) p)
      .collect(Collectors.toList());
    return list;
  }


}
