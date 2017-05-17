package fr.mgargadennec.blossom.core.common.actuator;

import org.springframework.boot.actuate.endpoint.mvc.EndpointMvcAdapter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;

/**
 * Created by Maël Gargadennnec on 17/05/2017.
 */
public class TraceStatisticsMvcEndpoint extends EndpointMvcAdapter {
  private final TraceStatisticsEndpoint endpoint;

  public TraceStatisticsMvcEndpoint(TraceStatisticsEndpoint endpoint) {
    super(endpoint);
    this.endpoint = endpoint;
  }


  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseBody
  public String invoke(@RequestParam(required = false) Long from,
                       @RequestParam(required = false) Long to) {
    return this.endpoint.invoke(from == null ? null : Instant.ofEpochMilli(from), to == null ? null : Instant.ofEpochMilli(to)).toString();
  }
}
