package fr.mgargadennec.blossom.ui.web.system.scheduler;

import fr.mgargadennec.blossom.core.scheduler.job.ScheduledJobServiceImpl;
import fr.mgargadennec.blossom.ui.menu.OpenedMenu;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController("/system/scheduler")
@OpenedMenu("schedulerManager")
public class SchedulerController {

  private final ScheduledJobServiceImpl scheduledJobService;

  @Autowired
  public SchedulerController(ScheduledJobServiceImpl scheduledJobService) {
    this.scheduledJobService = scheduledJobService;
  }

  @GetMapping
  public ModelAndView scheduler(Model model) throws SchedulerException {
    model.addAttribute("scheduler", this.scheduledJobService.getScheduler());
    model.addAttribute("groups", this.scheduledJobService.getGroups());
    return new ModelAndView("system/scheduler/scheduler", model.asMap());
  }

  @GetMapping("/{group}")
  public ModelAndView tasks(Model model, @PathVariable String group) throws SchedulerException {
    model.addAttribute("scheduler", this.scheduledJobService.getScheduler());
    model.addAttribute("jobInfos", this.scheduledJobService.getAll(group));
    return new ModelAndView("system/scheduler/list", model.asMap());
  }

  @GetMapping("/{group}/{name}")
  public ModelAndView task(Model model, @PathVariable String group, @PathVariable String name) throws SchedulerException {
    model.addAttribute("scheduler", this.scheduledJobService.getScheduler());
    model.addAttribute("jobInfo", this.scheduledJobService.getOne(JobKey.jobKey(name, group)));
    return new ModelAndView("system/scheduler/detail", model.asMap());
  }

  @PostMapping("/_changeState")
  @ResponseStatus(HttpStatus.OK)
  public void scheduler(Model model, @RequestParam("state") boolean state) throws SchedulerException {
    this.scheduledJobService.changeState(state);
  }


}
