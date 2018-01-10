package fr.blossom.ui.web;

import fr.blossom.ui.stereotype.BlossomController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@BlossomController
@RequestMapping("/public/status")
public class StatusController {

  private static final Logger LOGGER = LoggerFactory.getLogger(StatusController.class);
  private final HealthEndpoint healthEndpoint;

  public StatusController(HealthEndpoint healthEndpoint) {
    this.healthEndpoint = healthEndpoint;
  }

  @GetMapping
  @ResponseBody
  public ResponseEntity<String> status() {
    Health health = healthEndpoint.health();
    if (health.getStatus().equals(Status.UP)) {
      return ResponseEntity.ok(health.getStatus().getCode());
    }
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
      .body(health.getStatus().getCode());
  }
}
