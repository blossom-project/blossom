package fr.mgargadennec.blossom.ui.api;

import fr.mgargadennec.blossom.ui.stereotype.BlossomApiController;
import org.springframework.boot.info.BuildProperties;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomApiController
public class BuildInfoApiController {
  private final BuildProperties buildProperties;

  public BuildInfoApiController(BuildProperties buildProperties) {
    this.buildProperties = buildProperties;
  }

  @RequestMapping("/build")
  public BuildProperties buildInfos() {
    return buildProperties;
  }

}
