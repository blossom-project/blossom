package com.blossomproject.ui.web;

import com.blossomproject.ui.stereotype.BlossomController;
import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@BlossomController
@RequestMapping("/public/status")
public class StatusController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);
  private final HealthEndpoint healthEndpoint;
  private final HealthAggregator healthAggregator;

  public StatusController(HealthEndpoint healthEndpoint) {
    this.healthEndpoint = healthEndpoint;
    healthAggregator = new OrderedHealthAggregator();
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


  @VisibleForTesting
  Health filteredDetails(Health health, List<String> excludes) {
    if (health.getDetails().isEmpty()) {
      return health;
    }

    Map<String, Health> filteredHealth = health
      .getDetails()
      .entrySet()
      .stream()
      .filter(mapEntry -> mapEntry.getValue() instanceof Health && !excludes.contains(mapEntry.getKey()))
      .collect(Collectors.toMap(Map.Entry::getKey, e -> filteredDetails((Health) e.getValue(), excludes)));

    return healthAggregator.aggregate(filteredHealth);
  }
}
