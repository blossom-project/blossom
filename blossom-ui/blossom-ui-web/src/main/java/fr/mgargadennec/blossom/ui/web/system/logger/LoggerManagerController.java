package fr.mgargadennec.blossom.ui.web.system.logger;

import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.springframework.boot.actuate.endpoint.LoggersEndpoint;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by Maël Gargadennnec on 16/05/2017.
 */
@BlossomController("/system/loggers")
@OpenedMenu("loggerManager")
public class LoggerManagerController {
  private final LoggersEndpoint loggersEndpoint;

  public LoggerManagerController(LoggersEndpoint loggersEndpoint) {
    this.loggersEndpoint = loggersEndpoint;
  }

  @GetMapping
  public ModelAndView loggers(@RequestParam(name = "q", defaultValue = "", required = false) String q, Model model) {
    Map<String, Object> loggers = loggersEndpoint.invoke();

    Map<String, LoggersEndpoint.LoggerLevels> loggerLevels = (Map<String, LoggersEndpoint.LoggerLevels>) loggers.get("loggers");
    loggerLevels = loggerLevels
      .entrySet()
      .stream()
      .filter(entry -> StringUtils.isEmpty(q) || entry.getKey().contains(q))
      .collect(Collectors.toMap(
        e -> e.getKey(),
        e -> e.getValue(),
        (u, v) -> {
          throw new IllegalStateException(String.format("Duplicate key %s", u));
        },
        LinkedHashMap::new));

    loggers.put("loggers", loggerLevels);

    model.addAttribute("loggers", loggers);
    model.addAttribute("q", q);

    return new ModelAndView("system/loggers/loggers", model.asMap());
  }


  @PostMapping("/{name}/{logLevel}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void loggers(@PathVariable("name") String name, @PathVariable("logLevel") LogLevel logLevel) {
    loggersEndpoint.setLogLevel(name, logLevel);
  }
}
