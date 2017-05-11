package fr.mgargadennec.blossom.ui.web.system.scheduler;

import fr.mgargadennec.blossom.core.scheduler.job.ScheduledJobServiceImpl;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.quartz.JobKey;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController("/system/scheduler")
public class SchedulerController {

  private final ScheduledJobServiceImpl scheduledJobService;

  @Autowired
  public SchedulerController(ScheduledJobServiceImpl scheduledJobService) {
    this.scheduledJobService = scheduledJobService;
  }

  @GetMapping
  public ModelAndView scheduler(Model model) throws SchedulerException {
    model.addAttribute("groups", this.scheduledJobService.getGroups());
    model.addAttribute("scheduler",this.scheduledJobService.getScheduler());
    return new ModelAndView("system/scheduler/scheduler", model.asMap());
  }

  @GetMapping("/{group}")
  public ModelAndView groupTasks(Model model, @PathVariable String group) throws SchedulerException {
    return new ModelAndView("system/scheduler/list", "jobInfos", this.scheduledJobService.getAll(group));
  }

  @GetMapping("/{group}/{name}")
  public ModelAndView scheduler(Model model, @PathVariable String group, @PathVariable String name) throws SchedulerException {
    return new ModelAndView("system/scheduler/detail", "jobInfo", this.scheduledJobService.getOne(JobKey.jobKey(name, group)));
  }


}
