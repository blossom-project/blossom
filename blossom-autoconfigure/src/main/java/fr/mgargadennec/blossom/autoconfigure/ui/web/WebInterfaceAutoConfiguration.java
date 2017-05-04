package fr.mgargadennec.blossom.autoconfigure.ui.web;

import fr.mgargadennec.blossom.ui.web.BuildInfoController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(BuildInfoController.class)
public class WebInterfaceAutoConfiguration {

  @Bean
  public BuildInfoController buildInfoController(BuildProperties buildProperties) {
    return new BuildInfoController(buildProperties);
  }

}
