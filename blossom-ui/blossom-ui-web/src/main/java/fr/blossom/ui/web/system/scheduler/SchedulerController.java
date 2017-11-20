package fr.blossom.ui.web.system.scheduler;

import fr.blossom.core.scheduler.job.ScheduledJobService;
import fr.blossom.ui.menu.OpenedMenu;
import fr.blossom.ui.stereotype.BlossomController;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController("/system/scheduler")
@OpenedMenu("schedulerManager")
@PreAuthorize("hasAuthority('system:scheduler:manager')")
public class SchedulerController {

  private final ScheduledJobService scheduledJobService;

  @Autowired
  public SchedulerController(ScheduledJobService scheduledJobService) {
    this.scheduledJobService = scheduledJobService;
  }

  @GetMapping
  public ModelAndView scheduler(Model model) throws SchedulerException {
    model.addAttribute("scheduler", this.scheduledJobService.getSchedulerInfo());
    model.addAttribute("groups", this.scheduledJobService.getGroups());
    return new ModelAndView("system/scheduler/scheduler", model.asMap());
  }

  @GetMapping("/{group}")
  public ModelAndView tasks(Model model, @PathVariable String group) throws SchedulerException {
    model.addAttribute("scheduler", this.scheduledJobService.getSchedulerInfo());
    model.addAttribute("jobInfos", this.scheduledJobService.getAll(group));
    return new ModelAndView("system/scheduler/list", model.asMap());
  }

  @GetMapping("/{group}/{name}")
  public ModelAndView task(Model model, @PathVariable String group, @PathVariable String name) throws SchedulerException {
    model.addAttribute("scheduler", this.scheduledJobService.getSchedulerInfo());
    model.addAttribute("jobInfo", this.scheduledJobService.getOne(JobKey.jobKey(name, group)));
    return new ModelAndView("system/scheduler/detail", model.asMap());
  }

  @PostMapping("/{group}/{name}/_execute")
  @ResponseBody
  public void executeTask(Model model, @PathVariable String group, @PathVariable String name) throws SchedulerException {
    scheduledJobService.execute(JobKey.jobKey(name, group));
  }

  @PostMapping("/_changeState")
  @ResponseStatus(HttpStatus.OK)
  public void scheduler(Model model, @RequestParam("state") boolean state) throws SchedulerException {
    this.scheduledJobService.changeState(state);
  }


}
