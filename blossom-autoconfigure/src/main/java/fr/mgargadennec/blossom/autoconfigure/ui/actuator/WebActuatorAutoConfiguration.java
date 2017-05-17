package fr.mgargadennec.blossom.autoconfigure.ui.actuator;

import fr.mgargadennec.blossom.core.common.actuator.ElasticsearchTraceRepository;
import fr.mgargadennec.blossom.core.common.actuator.TraceStatisticsEndpoint;
import fr.mgargadennec.blossom.core.common.actuator.TraceStatisticsMvcEndpoint;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Maël Gargadennnec on 11/05/2017.
 */
@Configuration
@PropertySource("actuator.properties")
public class WebActuatorAutoConfiguration {

  @Bean
  @ConditionalOnBean(TraceStatisticsEndpoint.class)
  public TraceStatisticsMvcEndpoint traceStatisticsMvcEndpoint(TraceStatisticsEndpoint endpoint) {
    return new TraceStatisticsMvcEndpoint(endpoint);
  }

}
