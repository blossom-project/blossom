package fr.mgargadennec.blossom.ui.web.system.build;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController("/system/build")
public class BuildInfoController {

  private final BuildProperties buildProperties;

  public BuildInfoController(BuildProperties buildProperties) {
    this.buildProperties = buildProperties;
  }

  @GetMapping
  public ModelAndView buildInfos() {
    return new ModelAndView("system/build/build", "build", buildProperties);
  }

}
