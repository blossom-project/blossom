package fr.mgargadennec.blossom.ui.web.system.dashboard;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController("/system/dashboard")
public class DashboardController {

  @GetMapping
  public ModelAndView dashboard() {
    return new ModelAndView("system/dashboard/dashboard");
  }

}
