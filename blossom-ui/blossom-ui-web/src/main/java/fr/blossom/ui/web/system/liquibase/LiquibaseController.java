package fr.blossom.ui.web.system.liquibase;

import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.liquibase.LiquibaseEndpoint;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController
@RequestMapping("/system/liquibase")
@OpenedMenu("liquibase")
@PreAuthorize("hasAuthority('system:liquibase:manager')")
public class LiquibaseController {

  private final static Logger logger = LoggerFactory.getLogger(LiquibaseController.class);

  private final LiquibaseEndpoint liquibaseEndpoint;

  public LiquibaseController(LiquibaseEndpoint liquibaseEndpoint) {
    this.liquibaseEndpoint = liquibaseEndpoint;
  }

  @GetMapping
  public ModelAndView liquibase(Model model) {
    return new ModelAndView("blossom/system/liquibase/liquibase", "reports", liquibaseEndpoint.liquibaseReports());
  }

}
