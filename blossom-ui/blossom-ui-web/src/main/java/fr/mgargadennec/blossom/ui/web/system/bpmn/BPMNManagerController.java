package fr.mgargadennec.blossom.ui.web.system.bpmn;

import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import java.util.List;
import java.util.stream.Collectors;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.repository.ProcessDefinition;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by maelg on 10/05/2017.
 */
@BlossomController("/system/bpmn")
@OpenedMenu("bpmnManager")
@PreAuthorize("hasAuthority('system:bpmn:manager')")
public class BPMNManagerController {
  private final ProcessEngine processEngine;

  private final static Logger LOGGER = LoggerFactory.getLogger(BPMNManagerController.class);


  public BPMNManagerController(ProcessEngine processEngine) {
    this.processEngine = processEngine;
  }

  @GetMapping
  public ModelAndView bpmn( Model model) {
    List<ProcessDefinition> processDefinitions = processEngine.getRepositoryService().createProcessDefinitionQuery().latestVersion().list();
    List<ProcessInstance> instances = processEngine.getRuntimeService().createProcessInstanceQuery().list();
    model.addAttribute("processDefinitions", processDefinitions);
    model.addAttribute("instances", instances.stream().collect(Collectors.groupingBy(ProcessInstance::getProcessDefinitionId, Collectors.counting())));
    return new ModelAndView("system/bpmn/bpmn", model.asMap());
  }
}
