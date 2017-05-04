package fr.mgargadennec.blossom.autoconfigure.ui.api;

import fr.mgargadennec.blossom.ui.api.BuildInfoApiController;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@Configuration
@ConditionalOnClass(BuildInfoApiController.class)
public class WebApiAutoConfiguration {

  @Bean
  public BuildInfoApiController buildInfoController(BuildProperties buildProperties) {
    return new BuildInfoApiController(buildProperties);
  }

}
