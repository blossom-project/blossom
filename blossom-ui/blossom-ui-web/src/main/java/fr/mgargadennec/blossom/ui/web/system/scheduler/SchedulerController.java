package fr.mgargadennec.blossom.ui.web.system.scheduler;

import fr.mgargadennec.blossom.core.scheduler.job.ScheduledJobServiceImpl;
import fr.mgargadennec.blossom.ui.stereotype.BlossomController;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * Created by Maël Gargadennnec on 04/05/2017.
 */
@BlossomController("/system/scheduler")
public class SchedulerController {

  private final ScheduledJobServiceImpl scheduledJobService;

  @Autowired
  public SchedulerController(ScheduledJobServiceImpl scheduledJobService ) {
    this.scheduledJobService = scheduledJobService;
  }

  @GetMapping
  public ModelAndView scheduler(Model model) throws SchedulerException {

    return new ModelAndView("system/scheduler/scheduler", "jobInfos", scheduledJobService.getAll());
  }

}
