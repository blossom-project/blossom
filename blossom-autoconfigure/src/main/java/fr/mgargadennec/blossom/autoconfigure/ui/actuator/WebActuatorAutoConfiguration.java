package fr.mgargadennec.blossom.autoconfigure.ui.actuator;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import fr.mgargadennec.blossom.core.common.actuator.TraceStatisticsEndpoint;
import fr.mgargadennec.blossom.core.common.actuator.TraceStatisticsMvcEndpoint;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
@PropertySource("classpath:/actuator.properties")
public class WebActuatorAutoConfiguration {

  @Bean
  @ConditionalOnBean(TraceStatisticsEndpoint.class)
  public TraceStatisticsMvcEndpoint traceStatisticsMvcEndpoint(TraceStatisticsEndpoint endpoint) {
    return new TraceStatisticsMvcEndpoint(endpoint);
  }

}
