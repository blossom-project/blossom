package com.blossomproject.ui.web.system.logger;

import com.blossomproject.core.common.utils.tree.TreeNode;
import com.blossomproject.ui.menu.OpenedMenu;
import com.blossomproject.ui.stereotype.BlossomController;
import java.util.Map;
import java.util.Optional;

import org.springframework.boot.actuate.logging.LoggersEndpoint;
import org.springframework.boot.logging.LogLevel;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 16/05/2017.
 */
@BlossomController
@RequestMapping("/system/loggers")
@OpenedMenu("loggerManager")
@PreAuthorize("hasAuthority('system:logger:manager')")
public class LoggerManagerController {

  private final static String ROOT_LOGGER = "ROOT";
  private final LoggersEndpoint loggersEndpoint;

  public LoggerManagerController(LoggersEndpoint loggersEndpoint) {
    this.loggersEndpoint = loggersEndpoint;
  }

  @GetMapping
  public ModelAndView loggers(
    @RequestParam(name = "q", defaultValue = "", required = false) String q, Model model) {
    return new ModelAndView("blossom/system/loggers/loggers");
  }


  @GetMapping("/{name:.+}")
  public ModelAndView loggerDetail(@PathVariable("name") String name, Model model) {
    LoggersEndpoint.LoggerLevels levels = loggersEndpoint.loggerLevels(name);

    model.addAttribute("logger", name);
    model.addAttribute("loggerLevels", levels);
    model.addAttribute("levels", loggersEndpoint.loggers().get("levels"));

    return new ModelAndView("blossom/system/loggers/logger-detail", model.asMap());
  }

  @PostMapping("/{name}/{logLevel}")
  @ResponseStatus(HttpStatus.OK)
  @ResponseBody
  public void loggers(@PathVariable("name") String name,
    @PathVariable("logLevel") LogLevel logLevel) {
    loggersEndpoint.configureLogLevel(name, logLevel);
  }

  @GetMapping(value = "/tree", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public @ResponseBody
  TreeNode<String> loggersTree(
    @RequestParam(name = "q", defaultValue = "", required = false) String q, Model model) {
    LoggersEndpoint.LoggerLevels root = loggersEndpoint.loggerLevels(ROOT_LOGGER);
    TreeNode<String> rootNode = new TreeNode<String>(ROOT_LOGGER, ROOT_LOGGER,
      root.getEffectiveLevel());

    Map<String, Object> loggers = loggersEndpoint.loggers();
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
            TreeNode<String> newNode = new TreeNode<String>(currentKey, keyParts[i], e.getValue().getEffectiveLevel());
            treeNode.addChild(newNode);
            treeNode = newNode;
          }
          i++;
        } while (i <= keyParts.length - 1);

      });
    return rootNode;
  }

}
