package com.blossom_project.ui.api;

import com.google.common.collect.Lists;
import com.blossom_project.ui.stereotype.BlossomApiController;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@BlossomApiController
@RequestMapping("/public/status")
public class StatusApiController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StatusApiController.class);
  private final HealthEndpoint healthEndpoint;

  public StatusApiController(HealthEndpoint healthEndpoint) {
    this.healthEndpoint = healthEndpoint;
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<Health> status(@RequestParam(value = "exclude", required = false, defaultValue = "") Optional<List<String>> excludes) {
    Health health = filteredDetails(healthEndpoint.health(), excludes.orElse(Lists.newArrayList()));
    if (health.getStatus().equals(Status.UP)) {
      return ResponseEntity.ok(health);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(health);
  }

  private Health filteredDetails(Health health, List<String> excludes) {
    Health.Builder builder = new Health.Builder(health.getStatus());

    health
      .getDetails()
      .entrySet()
      .stream()
      .filter(e -> e.getValue() instanceof Health && !excludes.contains(e.getKey()))
      .forEach(
        e -> builder.withDetail(e.getKey(), filteredDetails((Health) e.getValue(), excludes))
      );

    return builder.build();
  }
}
