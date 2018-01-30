package fr.blossom.autoconfigure.core;

import org.camunda.bpm.spring.boot.starter.CamundaBpmAutoConfiguration;
import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.camunda.bpm.spring.boot.starter.rest.CamundaBpmRestJerseyAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
@PropertySource("classpath:/camunda.properties")
@ConditionalOnClass(CamundaBpmAutoConfiguration.class)
@AutoConfigureBefore({HibernateJpaAutoConfiguration.class, CamundaBpmAutoConfiguration.class,
  CamundaBpmRestJerseyAutoConfiguration.class})
public class CamundaAutoConfiguration {

  @Configuration
  @EnableProcessApplication
  public static class DefaultCamundaActivation {

  }
}
