package com.blossomproject.ui.supervision;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/blossom/public/status")
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
  public ResponseEntity<Health> status(
    @RequestParam(value = "exclude", required = false, defaultValue = "") Optional<List<String>> excludes,
    @RequestParam(value = "include", required = false, defaultValue = "") Optional<List<String>> includes) {
    Health health = filteredDetails(healthEndpoint.health(), excludes.orElse(Lists.newArrayList()));
    if (includes.isPresent() && !includes.get().isEmpty()) {
      health = includedDetails(health, includes
          .get()
          .stream()
          .map(String::toLowerCase)
          .map(s -> toString().isEmpty() ? "." : "." + s.toLowerCase())
          .collect(Collectors.toList()),
        "");
    }
    if (health.getStatus().equals(Status.UP)) {
      return ResponseEntity.ok(health);
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(health);
  }


  @VisibleForTesting
  Health filteredDetails(Health health, List<String> excludes) {
    Map<String, Health> filteredHealth = health
      .getDetails()
      .entrySet()
      .stream()
      .filter(mapEntry -> mapEntry.getValue() instanceof Health && !excludes.contains(mapEntry.getKey()))
      .collect(Collectors.toMap(Map.Entry::getKey, e -> filteredDetails((Health) e.getValue(), excludes)));

    if (filteredHealth.isEmpty()) {
      return Health.status(health.getStatus()).build();
    }

    return healthAggregator.aggregate(filteredHealth);
  }

  @VisibleForTesting
  Health includedDetails(Health health, List<String> includes, String currentDepth) {

    if (health.getDetails().isEmpty()) {
      return health;
    }

    Map<String, Health> filteredHealth = health
      .getDetails()
      .entrySet()
      .stream()
      .filter(mapEntry -> mapEntry.getValue() instanceof Health && includes.stream().anyMatch(pattern -> pattern.startsWith(currentDepth + "." + mapEntry.getKey().toLowerCase())))
      .map(mapEntry -> {
        if (includes.stream().anyMatch(pattern -> pattern.equals(currentDepth + "." + mapEntry.getKey().toLowerCase()))) {
          return new AbstractMap.SimpleEntry<>(mapEntry.getKey(), (Health) mapEntry.getValue());
        } else {
          return new AbstractMap.SimpleEntry<>(mapEntry.getKey(), includedDetails((Health) mapEntry.getValue(), includes, currentDepth + "." + mapEntry.getKey().toLowerCase()));
        }
      })
      .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    return healthAggregator.aggregate(filteredHealth);
  }
}
