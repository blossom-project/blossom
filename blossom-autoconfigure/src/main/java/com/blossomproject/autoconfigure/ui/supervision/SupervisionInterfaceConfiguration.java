package com.blossomproject.autoconfigure.ui.supervision;

import com.blossomproject.ui.supervision.StatusController;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration("BlossomSupervisionInterfaceConfiguration")
@ConditionalOnWebApplication
@ConditionalOnClass({StatusController.class})
public class SupervisionInterfaceConfiguration {
  @Bean
  public StatusController statusController(HealthEndpoint healthEndpoint) {
    return new StatusController(healthEndpoint);
  }
}
