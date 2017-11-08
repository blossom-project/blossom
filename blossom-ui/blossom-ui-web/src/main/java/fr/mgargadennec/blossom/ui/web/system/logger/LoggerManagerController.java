package fr.mgargadennec.blossom.ui.web.system.logger;

import fr.mgargadennec.blossom.core.common.utils.tree.TreeNode;
import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import java.util.Map;
import java.util.Optional;
import org.springframework.boot.actuate.endpoint.LoggersEndpoint;
import org.springframework.boot.actuate.endpoint.LoggersEndpoint.LoggerLevels;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 16/05/2017.
 */
@BlossomController("/system/loggers")
@OpenedMenu("loggerManager")
public class LoggerManagerController {

  private final static String ROOT_LOGGER = "ROOT";
  private final LoggersEndpoint loggersEndpoint;

  public LoggerManagerController(LoggersEndpoint loggersEndpoint) {
    this.loggersEndpoint = loggersEndpoint;
  }

  @GetMapping
  public ModelAndView loggers(
    @RequestParam(name = "q", defaultValue = "", required = false) String q, Model model) {
    return new ModelAndView("system/loggers/loggers");
  }


  @GetMapping("/{name:.+}")
  public ModelAndView loggerDetail(@PathVariable("name") String name, Model model) {
    LoggerLevels levels = loggersEndpoint.invoke(name);

    model.addAttribute("logger", name);
    model.addAttribute("loggerLevels", levels);
    model.addAttribute("levels", loggersEndpoint.invoke().get("levels"));

    return new ModelAndView("system/loggers/logger-detail", model.asMap());
  }

  @PostMapping("/{name}/{logLevel}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void loggers(@PathVariable("name") String name,
    @PathVariable("logLevel") LogLevel logLevel) {
    loggersEndpoint.setLogLevel(name, logLevel);
  }

  @GetMapping(value = "/tree", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody
  TreeNode<String> loggersTree(
    @RequestParam(name = "q", defaultValue = "", required = false) String q, Model model) {
    LoggerLevels root = loggersEndpoint.invoke(ROOT_LOGGER);
    TreeNode<String> rootNode = new TreeNode<String>(ROOT_LOGGER, ROOT_LOGGER,
      root.getEffectiveLevel());

    Map<String, Object> loggers = loggersEndpoint.invoke();
    Map<String, LoggersEndpoint.LoggerLevels> loggerLevels = (Map<String, LoggersEndpoint.LoggerLevels>) loggers
      .get("loggers");

    loggerLevels.entrySet()
      .stream()
      .filter(e -> !e.getKey().equals(ROOT_LOGGER))
      .filter(entry -> StringUtils.isEmpty(q) || entry.getKey().contains(q))
      .forEach(e -> {
        TreeNode<String> treeNode = rootNode;
        String[] keyParts = e.getKey().split("\\.");
        int i = 0;
        String currentKey = null;
        do {
          currentKey = currentKey != null ? currentKey + "." + keyParts[i] : keyParts[i];
          Optional<TreeNode<String>> child = treeNode.findChildWithId(currentKey);
          if (child.isPresent()) {
            treeNode = child.get();
          } else {
            TreeNode<String> newNode = new TreeNode<String>(currentKey, keyParts[i],
              ((LoggerLevels) e.getValue()).getEffectiveLevel());
            treeNode.addChild(newNode);
            treeNode = newNode;
          }
          i++;
        } while (i <= keyParts.length - 1);

      });
    return rootNode;
  }

}
