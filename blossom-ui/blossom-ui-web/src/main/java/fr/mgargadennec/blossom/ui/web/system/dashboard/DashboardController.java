package fr.mgargadennec.blossom.ui.web.system.dashboard;

import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.boot.actuate.endpoint.HealthEndpoint;
import org.springframework.boot.actuate.endpoint.MetricsEndpoint;
import org.springframework.boot.actuate.health.Health;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController("/system/dashboard")
public class DashboardController {
  private final HealthEndpoint healthEndpoint;
  private final MetricsEndpoint metricsEndpoint;

  public DashboardController(HealthEndpoint healthEndpoint, MetricsEndpoint metricsEndpoint) {
    this.healthEndpoint = healthEndpoint;
    this.metricsEndpoint = metricsEndpoint;
  }


  @GetMapping
  public ModelAndView dashboard() {
    return new ModelAndView("system/dashboard/dashboard");
  }


  @GetMapping("/status")
  public ModelAndView status() {
    Health health = healthEndpoint.invoke();
    return new ModelAndView("system/dashboard/panel/status", "health", health);
  }


  @GetMapping("/memory")
  public ModelAndView memory() {
    Map metrics = metricsEndpoint.invoke();
    return new ModelAndView("system/dashboard/panel/memory", "metrics", metrics);
  }

}
