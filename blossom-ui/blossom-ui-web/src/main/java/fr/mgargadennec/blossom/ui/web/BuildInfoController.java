package fr.mgargadennec.blossom.ui.web;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController
public class BuildInfoController {

  private final BuildProperties buildProperties;

  public BuildInfoController(BuildProperties buildProperties) {
    this.buildProperties = buildProperties;
  }

  @RequestMapping("/build")
  @ResponseBody
  public String buildInfos() {
    return buildProperties.toString();
  }

}
